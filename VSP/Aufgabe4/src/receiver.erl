-module(receiver).
-import(werkzeug, [get_config_value/2, logging/2, logstop/0, openSe/2, openSeA/2, openRec/3, openRecA/3, createBinaryS/1, createBinaryD/1, createBinaryT/1, createBinaryNS/1, concatBinary/4, message_to_string/1, shuffle/1, timeMilliSecond/0, reset_timer/3, compareNow/2, getUTC/0, compareUTC/2, now2UTC/1, type_is/1, to_String/1, bestimme_mis/2, testeMI/2]).

%% API
-export([start/0]).

%% Schnittstellen

%% MAIN
% startet und initialisiert das ReceiveModul und alle benötigten anderen Module

start() ->

  Socket = openRec(MultiCast, Addr, Port),
  gen_udp:controlling_process(Socket, self()),% diesen Prozess PidRec (als Nebenläufigenprozess gestartet) bekannt geben mit

%TODO CLOSE SOCKET
%gen_udp:close(Socket).

  true.

% Empfang - 1: HauptReceiveBlock für die Anwendung
waitForMessage(Logfile, Sender, HBQ, Clock) ->
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
      waitForMessage(Logfile, SenderNeu, HBQ, Clock);

    frameTimer ->
      % TODO: neuer Frame hat begonnen
      SenderNeu = sender:frameStarts(Sender, HBQ, Clock),
      waitForMessage(Logfile, SenderNeu, HBQ, Clock);

    sendTimer ->
      % TODO: Nachricht soll gesendet werden
      SenderNeu = sender:send(Sender, HBQ, Clock),
      waitForMessage(Logfile, SenderNeu, HBQ, Clock);

    Any ->
      logging(Logfile, "received something wrong: " ++ to_String(Any) ++ "\n"),
      waitForMessage(Logfile, Sender, HBQ, Clock)
  end,
  true.