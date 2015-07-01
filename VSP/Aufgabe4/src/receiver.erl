-module(receiver).
-import(werkzeug, [get_config_value/2, logging/2, logstop/0, openSe/2, openSeA/2, openRec/3, openRecA/3, createBinaryS/1, createBinaryD/1, createBinaryT/1, createBinaryNS/1, concatBinary/4, message_to_string/1, shuffle/1, timeMilliSecond/0, reset_timer/3, compareNow/2, getUTC/0, compareUTC/2, now2UTC/1, type_is/1, to_String/1, bestimme_mis/2, testeMI/2]).

%% API
-export([start/1]).

-define(LOGFILE, "logfile.log").

%% Schnittstellen

%% MAIN
% startet und initialisiert das ReceiveModul und alle benoetigten anderen Module

start([InterfaceStr, PortStr, StationTyp, ClockOffsetStr]) ->
  {Port, _} = string:to_integer(PortStr),
  {ClockOffset, _} = string:to_integer(ClockOffsetStr),
  %WadisMeeep = {141,22,27,102},  %DEBUG
  WadisMeeep = infToAddr(InterfaceStr),

  logging(?LOGFILE, "Parameter eingelesen\n"),

  Socket = openRec({225,10,1,2}, WadisMeeep, Port),
  gen_udp:controlling_process(Socket, self()),% diesen Prozess PidRec (als Nebenlaeufigenprozess gestartet) bekannt geben mit

  logging(?LOGFILE, "ReceiveSocket geoeffnet\n"),

  Data = datenquelle:getNextData(),
  Msg = message:newMessage(null, Data),

  logging(?LOGFILE, "StationNr von DummyMsg eingelesen\n"),

  Sender = sender:newSender(StationTyp, WadisMeeep),
  logging(?LOGFILE, "Sender gestartet\n"),

  HBQ = hbqueue:initHBQueue(message:getStation(Msg)),
  logging(?LOGFILE, "HBQ gestartet\n"),

  Clock = clock:initClock(ClockOffset, self()),
  logging(?LOGFILE, "Clock gestartet\n"),

  % goto receive Schleife
  waitForMessage(?LOGFILE, Sender, HBQ, Clock, Socket).

% Empfang - 1: HauptReceiveBlock fuer die Anwendung
waitForMessage(?LOGFILE, Sender, HBQ, Clock, Socket) ->
  logging(?LOGFILE, "Starte Receive Block\n"),
  receive
    {udp, _ReceiveSocket, _IP, _InPortNo, Packet} ->
      logging(?LOGFILE, "UDP Nachricht empfangen\n"),
      %{StationTyp,Nutzdaten,Slot,Timestamp} = werkzeug:message_to_string(Packet);
      Message = message:packetToMessageObj(Packet),
      %Sollte erledigt sein: TODO, irgendwas mit der empfangenen Nachricht anfangen
      {Collision, HBQNeu} = hbqueue:push(HBQ, Message),
      if
        Collision =:= true ->
          SenderNeu = sender:resetSendSlot(Sender);
        true -> SenderNeu = Sender
      end,

      StationTyp = message:getStationTyp(Message),
      case StationTyp of
        "A" ->
          ClockNeu = clock:synchronize(Clock, message:getTime(Message));
        _ ->
          ClockNeu = Clock
      end,

      waitForMessage(?LOGFILE, SenderNeu, HBQNeu, ClockNeu, Socket);

    frameTimer ->
      logging(?LOGFILE, "FrameTimer hat Timeout gemeldet.\n"),
      % TODO: neuer Frame hat begonnen
      CurFrame = clock:getCurFrame(Clock),
      SenderNeu = sender:frameStarts(CurFrame, Sender, HBQ, Clock),

      hbqueue:resetHBQForNewFrame(HBQ, CurFrame),

      clock:startFrameTimer(Clock),

      waitForMessage(?LOGFILE, SenderNeu, HBQ, Clock, Socket);

    sendTimer ->
      logging(?LOGFILE, "SendTimer hat Timeout gemeldet.\n"),
      % TODO: Nachricht soll gesendet werden
      SenderNeu = sender:send(Sender, HBQ, Clock),
      waitForMessage(?LOGFILE, SenderNeu, HBQ, Clock, Socket);

    Any ->
      logging(?LOGFILE, "received something wrong: " ++ to_String(Any) ++ "\n"),
      waitForMessage(?LOGFILE, Sender, HBQ, Clock, Socket)
  end,
  gen_udp:close(Socket),
  true.

%% interne Hilfsmethoden
% liefert die Addresse zum angegebenen Netzwerkinterface
infToAddr(Interface) ->
  {ok, Devices} = inet:getifaddrs(),
  findDevice(Interface, Devices).

findDevice(_Search, []) -> null;

findDevice(Search, [{Search, Infos} | _Devices]) ->
  getAddrFromInfo(Infos);

findDevice(Search, [_First | Devices]) ->
  findDevice(Search, Devices).

getAddrFromInfo([]) -> null;

getAddrFromInfo([{addr, {One, Two, Three, Four}} | _]) ->
  {One, Two, Three, Four};

getAddrFromInfo([_ | Info]) ->
  getAddrFromInfo(Info).