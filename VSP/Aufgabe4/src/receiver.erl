-module(receiver).
-import(werkzeug, [get_config_value/2, logging/2, logstop/0, openSe/2, openSeA/2, openRec/3, openRecA/3, createBinaryS/1, createBinaryD/1, createBinaryT/1, createBinaryNS/1, concatBinary/4, message_to_string/1, shuffle/1, timeMilliSecond/0, reset_timer/3, compareNow/2, getUTC/0, compareUTC/2, now2UTC/1, type_is/1, to_String/1, bestimme_mis/2, testeMI/2]).

%% API
-export([start/1]).

%% Schnittstellen

%% MAIN
% startet und initialisiert das ReceiveModul und alle benoetigten anderen Module

start([WadisMeeep, Port, StationTyp, ClockOffset]) ->
  Socket = openRec({142,22,78,197}, WadisMeeep, Port),
  gen_udp:controlling_process(Socket, self()),% diesen Prozess PidRec (als Nebenlaeufigenprozess gestartet) bekannt geben mit

  Data = datenquelle:getNextData(),
  Msg = message:newMessage(null, Data),

  Logfile = "Logfile.log",
  Sender = sender:newSender(StationTyp, WadisMeeep),
  HBQ = hbqueue:initHBQueue(message:getStation(Msg)),
  Clock = clock:initClock(ClockOffset, self()),

  % goto receive Schleife
  waitForMessage(Logfile, Sender, HBQ, Clock, Socket).

% Empfang - 1: HauptReceiveBlock fuer die Anwendung
waitForMessage(Logfile, Sender, HBQ, Clock, Socket) ->
  receive
    {udp, _ReceiveSocket, _IP, _InPortNo, Packet} ->
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
        'A' ->
          ClockNeu = clock:synchronize(Clock, message:getTime(Message));
        _ ->
          ClockNeu = Clock
      end,

      waitForMessage(Logfile, SenderNeu, HBQNeu, ClockNeu, Socket);

    frameTimer ->
      % TODO: neuer Frame hat begonnen
      CurFrame = clock:getCurFrame(Clock),
      SenderNeu = sender:frameStarts(CurFrame, Sender, HBQ, Clock),

      hbqueue:resetHBQForNewFrame(HBQ, CurFrame),

      waitForMessage(Logfile, SenderNeu, HBQ, Clock, Socket);

    sendTimer ->
      % TODO: Nachricht soll gesendet werden
      SenderNeu = sender:send(Sender, HBQ, Clock),
      waitForMessage(Logfile, SenderNeu, HBQ, Clock, Socket);

    Any ->
      logging(Logfile, "received something wrong: " ++ to_String(Any) ++ "\n"),
      waitForMessage(Logfile, Sender, HBQ, Clock, Socket)
  end,
  gen_udp:close(Socket),
  true.