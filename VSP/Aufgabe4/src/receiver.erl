-module(receiver).
-import(werkzeug, [get_config_value/2, logging/2, logstop/0, openSe/2, openSeA/2, openRec/3, openRecA/3, createBinaryS/1, createBinaryD/1, createBinaryT/1, createBinaryNS/1, concatBinary/4, message_to_string/1, shuffle/1, timeMilliSecond/0, reset_timer/3, compareNow/2, getUTC/0, compareUTC/2, now2UTC/1, type_is/1, to_String/1, bestimme_mis/2, testeMI/2]).

%% API
-export([start/1]).

-define(LOGFILE, "logfile.log").

%% Schnittstellen

%% MAIN
% startet und initialisiert das ReceiveModul und alle benoetigten anderen Module
start([InterfaceStr, PortStr, StationTyp]) ->
  start([InterfaceStr, PortStr, StationTyp, "0"]);

start([InterfaceStr, PortStr, StationTyp, ClockOffsetStr]) ->
  {Port, _} = string:to_integer(PortStr),
  {ClockOffset, _} = string:to_integer(ClockOffsetStr),
  %WadisMeeep = {141,22,27,102},  %DEBUG
  WadisMeeep = infToAddr(InterfaceStr),

  %logging(?LOGFILE, "Parameter eingelesen\n"),

  Socket = openRecA({225, 10, 1, 2}, WadisMeeep, Port),
  gen_udp:controlling_process(Socket, self()),% diesen Prozess PidRec (als Nebenlaeufigenprozess gestartet) bekannt geben mit

  %logging(?LOGFILE, "ReceiveSocket geoeffnet\n"),

  Datenquelle = datenquelle:startDatenquelle(),
  %logging(?LOGFILE, "Datenquelle gestartet\n"),

  Data = datenquelle:getNextData(Datenquelle),
  Msg = message:newMessage(null, Data),

  %logging(?LOGFILE, "StationNr von DummyMsg eingelesen\n"),

  Sender = sender:newSender(StationTyp, WadisMeeep, Port),
  %logging(?LOGFILE, "Sender gestartet\n"),

  MyStation = message:getStation(Msg),
  HBQ = hbqueue:initHBQueue(MyStation),
  %logging(?LOGFILE, "HBQ gestartet\n"),

  Clock = clock:initClock(ClockOffset, self()),
  %logging(?LOGFILE, "Clock gestartet\n"),

  % goto receive Schleife
  waitForMessage(0, 0, MyStation, Sender, HBQ, Clock, Socket, Datenquelle, true).

% Empfang - 1: HauptReceiveBlock fuer die Anwendung
waitForMessage(FrameCounter, SendFrameCounter, MyStation, Sender, HBQ, Clock, Socket, Datenquelle, WaitForFirstFullFrame) ->
  receive
    {udp, _ReceiveSocket, _IP, _InPortNo, Packet} ->
      %logging(?LOGFILE, "UDP Nachricht empfangen\n"),
      %{StationTyp,Nutzdaten,Slot,Timestamp} = werkzeug:message_to_string(Packet);
      Message = message:packetToMessageObj(Packet),

      StationTyp = message:getStationTyp(Message),
      case StationTyp of
        "A" ->
          ClockNeu = clock:synchronize(Clock, message:getTime(Message));
        _ ->
          ClockNeu = Clock
      end,

      {Collision, HBQNeu} = hbqueue:push(HBQ, Message),
      if
        Collision =:= true ->
          SenderNeu = sender:resetSendSlot(Sender);
        true -> SenderNeu = Sender
      end,

      waitForMessage(FrameCounter, SendFrameCounter, MyStation, SenderNeu, HBQNeu, ClockNeu, Socket, Datenquelle, WaitForFirstFullFrame);

    frameTimer when (WaitForFirstFullFrame =:= true) ->
      % wir warten noch darauf, den ersten vollen Frame zuhoeren zu koennen
      % jetzt ist der erste Frame rum

      CurFrame = clock:getCurFrame(Clock),
      HBQNeu = hbqueue:resetHBQForNewFrame(HBQ, CurFrame),
      ClockNeu = clock:startFrameTimer(Clock),

      waitForMessage(FrameCounter + 1, SendFrameCounter, MyStation, Sender, HBQNeu, ClockNeu, Socket, Datenquelle, false);

    frameTimer ->
      logging(?LOGFILE, to_String(MyStation) ++ ": Bisher in " ++ to_String(SendFrameCounter) ++ " Frames gesendet und in " ++ to_String(FrameCounter - SendFrameCounter) ++ " nicht gesendet.\n"),
      CurFrame = clock:getCurFrame(Clock),
      {ClockNeu, SenderNeu} = sender:frameStarts(CurFrame, Sender, HBQ, Clock, Datenquelle),

      HBQNeu = hbqueue:resetHBQForNewFrame(HBQ, CurFrame),

      ClockNeu2 = clock:startFrameTimer(ClockNeu),

      waitForMessage(FrameCounter + 1, SendFrameCounter, MyStation, SenderNeu, HBQNeu, ClockNeu2, Socket, Datenquelle, WaitForFirstFullFrame);

    sendTimer when (WaitForFirstFullFrame =:= true) ->
      % sollte eigentlich nie passieren, falls doch -> einfach ignorieren
      waitForMessage(FrameCounter, SendFrameCounter, MyStation, Sender, HBQ, Clock, Socket, Datenquelle, WaitForFirstFullFrame);

    sendTimer ->
      %logging(?LOGFILE, "SendTimer hat Timeout gemeldet.\n"),
      ClockNeu = clock:resetSendTimer(Clock),

      {MsgSended, SenderNeu} = sender:send(Sender, HBQ, ClockNeu),

      if
        (MsgSended =:= true) -> NewSendFrameCounter = SendFrameCounter + 1;
        true -> NewSendFrameCounter = SendFrameCounter
      end,
      waitForMessage(FrameCounter, NewSendFrameCounter, MyStation, SenderNeu, HBQ, ClockNeu, Socket, Datenquelle, WaitForFirstFullFrame);

    _ ->
      %logging(?LOGFILE, "received something wrong: " ++ to_String(Any) ++ "\n"),
      waitForMessage(FrameCounter, SendFrameCounter, MyStation, Sender, HBQ, Clock, Socket, Datenquelle, WaitForFirstFullFrame)
  end,
  gen_udp:close(Socket),
  true.

%% interne Hilfsmethoden
% liefert die Addresse zum angegebenen Netzwerkinterface
infToAddr(Interface) ->
  %logging(?LOGFILE, "Suche nach Interface " ++ Interface ++ "\n"),
  {ok, Devices} = inet:getifaddrs(),
  findDevice(Interface, Devices).

findDevice(_Search, []) -> null;

findDevice(Search, [{Search, Infos} | _Devices]) ->
  %logging(?LOGFILE, "Interface in Device-Liste gefunden\n"),
  getAddrFromInfo(Infos);

findDevice(Search, [_First | Devices]) ->
  findDevice(Search, Devices).

getAddrFromInfo([]) -> null;

getAddrFromInfo([{addr, {One, Two, Three, Four}} | _]) ->
  {One, Two, Three, Four};

getAddrFromInfo([_ | Info]) ->
  getAddrFromInfo(Info).