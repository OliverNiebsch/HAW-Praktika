-module(ggt).
-import(werkzeug, [get_config_value/2, logging/2, logstop/0, openSe/2, openSeA/2, openRec/3, openRecA/3, createBinaryS/1, createBinaryD/1, createBinaryT/1, createBinaryNS/1, concatBinary/4, message_to_string/1, shuffle/1, timeMilliSecond/0, reset_timer/3, compareNow/2, getUTC/0, compareUTC/2, now2UTC/1, type_is/1, to_String/1, bestimme_mis/2, testeMI/2]).
-export([start/9, setTimer/3]).

%Initialisierung : .
start(ArbeitsZeit, Timeout, Quota, Praktikumsgruppe, Teamnummer, GGTNummer, StarterNummer, Nameservice, KoordinatorName) ->
  %timer:sleep(500),
  ProzessName = list_to_atom(to_String(Praktikumsgruppe) ++ to_String(Teamnummer) ++ to_String(GGTNummer) ++ to_String(StarterNummer)),
  Logfile = to_String(ProzessName) ++ "@" ++ to_String(node()) ++ ".log",

  register(ProzessName, self()),%registerlocal: Diagram 1 : 4.1
  rebindToNameservice(Nameservice, ProzessName),%rebind nameservice: Diagram 1:. 4.2

  KoordinatorPID = getProzessPIDByName(KoordinatorName, Nameservice, Logfile),
  KoordinatorPID ! {hello, ProzessName}, % hello an koordinator : Diagramm 1 : 4.3

  TimerPID = initTimer(Timeout*500), %Starte Timer mit halben Timeout. Umwandlung Sekunden > MS
  receiveFunction(noNeighbourYet, noNeighbourYet, waitingForStart, Quota, 0, noMi, TimerPID, ArbeitsZeit, Nameservice, KoordinatorName, ProzessName, Logfile),

  true.



rebindToNameservice(Nameservice, ProzessName) ->
  Nameservice ! {self(), {rebind, ProzessName, node()}},
  receive ok ->
    io:format("..rebind.done.\n");
    kill ->
      self() ! kill
  end.

getProzessPIDByName(ProzessName, Nameservice, Logfile) ->
%Koordinator holen
  Nameservice ! {self(), {lookup, ProzessName}},
  receive
    not_found ->
      logging(Logfile, to_String(ProzessName) ++ " not_found.\n"),
      pid_not_found;

    {pin, PID} ->
      PID;
  %logging(Logfile, to_String(ProzessName) ++ ": ",
    kill ->
      self() ! kill
  %killMe()
  end.


%halfTimeoutFlag is set to true, if 50% of timeout passed.
%In that case terminateVotes are answered with voteYes
%Otherwise discard msg

% halfTimeoutFlag
% == waitingForStart >> Noch nichts gestartet. Muss erst einmal mit setpm initialisiert werden, bevor andere Nachrichten akzeptiert werden.
% == notimeout >> Noch kein Timeout gekommen
% == halftimeout >> Erstes mal ein Timeout
% == voteRunning >> Es läuft gerade ein Vote von diesem Prozess
% == voteSuccess >> Vote war bereits erfolgreich. Keine neuen Votes starten aber auf votes mit voteYes antworten oder bei eingehenden zahlen neu rechnen

receiveFunction(LeftN, RightN, HalfTimeoutFlag, Quota, CurrentVoteCount, CurrentMi, TimerPID, ArbeitsZeit, Nameservice, KoordinatorName, MeinProzessName, Logfile) ->

  %CurrentVoteCount auf 0 wenn nicht gerade vote running ist.

  receive
    {FromPID, pingGGT} ->
      FromPID ! {pongGGT, MeinProzessName},
      logging(Logfile, ".... * .... Ping from: " ++ to_String(FromPID) ++ ".... * .... \n"),
      receiveFunction(LeftN, RightN, HalfTimeoutFlag, Quota, CurrentVoteCount, CurrentMi, TimerPID, ArbeitsZeit, Nameservice, KoordinatorName, MeinProzessName, Logfile);

    {FromPID, {vote, InitiatorName}} when (HalfTimeoutFlag =/= notimeout) and (HalfTimeoutFlag =/= waitingForStart) -> %Wenn halftimeout ODER voteRunning > Andere votes bestätigen
      FromPID ! {voteYes, InitiatorName}, %Diagramm 3 : 1.1.1
      logging(Logfile, ".: VOTE YES :. for " ++ to_String(InitiatorName) ++ ".: VOTE YES :. \n"),
      receiveFunction(LeftN, RightN, HalfTimeoutFlag, Quota, CurrentVoteCount, CurrentMi, TimerPID, ArbeitsZeit, Nameservice, KoordinatorName, MeinProzessName, Logfile);

    {voteYes, MeinProzessName} when HalfTimeoutFlag =:= voteRunning -> %Diagramm 3 : 2
      % Quote berechnen ob schon genug
      CurrentVoteCountNeu = CurrentVoteCount + 1, %Diagramm 3 : 2.1
      if
        CurrentVoteCountNeu >= Quota -> %VOTE SUCCESS > Fertig...
          Koordinator = getProzessPIDByName(KoordinatorName, Nameservice, Logfile),
          Koordinator ! {self(), briefterm, {MeinProzessName, CurrentMi, timeMilliSecond()}}, %Diagramm 3 : 2.2
          logging(Logfile, ":.*.: VOTE SUCCESSFUL: newState: voteSuccess. Koordinator briefterm " ++ ":.*.: \n"),
          receiveFunction(LeftN, RightN, voteSuccess, Quota, 0, CurrentMi, TimerPID, ArbeitsZeit, Nameservice, KoordinatorName, MeinProzessName, Logfile)
      end,

      receiveFunction(LeftN, RightN, HalfTimeoutFlag, Quota, CurrentVoteCountNeu, CurrentMi, TimerPID, ArbeitsZeit, Nameservice, KoordinatorName, MeinProzessName, Logfile);

    {sendy, Y} when HalfTimeoutFlag =/= waitingForStart ->
      %Start algorithm
      logging(Logfile, "Y erhalten: " ++ to_String(Y) ++ "\n"),
      MiNeu = berechneMi(CurrentMi, Y), %Diagramm 2 : 3.1
      timer:sleep(ArbeitsZeit * 1000),%Diagramm 2 : 3.2

      if CurrentMi > MiNeu ->
        %Mi ist kleiner geworden
        logging(Logfile, "Mi kleiner geworden: MiAlt: " ++ to_String(CurrentMi) ++ " // MiNeu:" ++ to_String(MiNeu) ++ "\n"),

        LeftNeighbourPID = getProzessPIDByName(LeftN, Nameservice, Logfile),
        if LeftNeighbourPID =/= pid_not_found ->
          logging(Logfile, ":......:  sendY an LinkenNachbarn: " ++ to_String(LeftN) ++ ":......: \n"),
          LeftNeighbourPID ! {sendy, MiNeu}; %Diagramm 2 : 3.5
          true ->
            logging(Logfile, ":C!!!!!!ARRGRHGRHGR  RechtenNachbar fehlt: \n")
        end,

        RightNeighbourPID = getProzessPIDByName(RightN, Nameservice, Logfile),
        if RightNeighbourPID =/= pid_not_found ->
          logging(Logfile, ":......:  sendY an RechtenNachbarn: " ++ to_String(RightN) ++ ":......: \n"),
          RightNeighbourPID ! {sendy, MiNeu};%Diagramm 2 : 3.4
          true ->
            logging(Logfile, ":C!!!!!!ARRGRHGRHGR  RechtenNachbar fehlt: \n")
        end,

        Koordinator = getProzessPIDByName(KoordinatorName, Nameservice, Logfile),
        Koordinator ! {briefmi, {MeinProzessName, MiNeu, werkzeug:timeMilliSecond()}}; %Diagramm 2 : 3.3

        true -> true
      end,

      resetTimer(TimerPID),
      receiveFunction(LeftN, RightN, notimeout, Quota, 0, MiNeu, TimerPID, ArbeitsZeit, Nameservice, KoordinatorName, MeinProzessName, Logfile);

    {setpm, MiNeu} ->
      logging(Logfile, "::-:-::  setpm mit Mi:" ++ to_String(MiNeu) ++ " erhalten  ::-:-::" ++ "\n"),
      %flush(),
      resetTimer(TimerPID),
      receiveFunction(LeftN, RightN, notimeout, Quota, 0, MiNeu, TimerPID, ArbeitsZeit, Nameservice, KoordinatorName, MeinProzessName, Logfile);
    kill ->
      logging(Logfile, "####### kill Message erhalten #######" ++ "\n"),
      %Kill this process
      killMe(Nameservice, MeinProzessName, TimerPID);
    timeoutMessage when HalfTimeoutFlag =:= notimeout -> %Erstes mal Timeout/2
      %Half timeout passed
      logging(Logfile, "** halbes Timeout erreicht **" ++ "\n"),
      receiveFunction(LeftN, RightN, halftimeout, Quota, CurrentVoteCount, CurrentMi, TimerPID, ArbeitsZeit, Nameservice, KoordinatorName, MeinProzessName, Logfile);

    timeoutMessage when HalfTimeoutFlag =:= halftimeout -> %Zweites mal Timeout/2
      %Second timeout/2 received -> Try to vote timeout
      logging(Logfile, "******** Timeout erreicht: StarteVote: ********" ++ "\n"),
      Nameservice ! {self(), {multicast, vote, MeinProzessName}}, %INIT TERMINATE VOTE %Diagramm 3 : 1
      receiveFunction(LeftN, RightN, voteRunning, Quota, 0, CurrentMi, TimerPID, ArbeitsZeit, Nameservice, KoordinatorName, MeinProzessName, Logfile);
    {FromPID, tellmi} ->
      FromPID ! {mi, CurrentMi},
      receiveFunction(LeftN, RightN, HalfTimeoutFlag, Quota, CurrentVoteCount, CurrentMi, TimerPID, ArbeitsZeit, Nameservice, KoordinatorName, MeinProzessName, Logfile);

    {setneighbours, LeftNNeu, RightNNeu} -> %Diagramm 1 : 6.
      logging(Logfile, "SetNeighbours:" ++ to_String(LeftNNeu) ++ to_String(" :::: ") ++ to_String(RightNNeu) ++ "\n"),
      receiveFunction(LeftNNeu, RightNNeu, waitingForStart, Quota, CurrentVoteCount, CurrentMi, TimerPID, ArbeitsZeit, Nameservice, KoordinatorName, MeinProzessName, Logfile);

    Any ->
      %Discard unwanted msg
      logging(Logfile, "/: DISCARDING UNWANTED MSG:" ++ to_String(Any) ++ "\n"),
      receiveFunction(LeftN, RightN, HalfTimeoutFlag, Quota, CurrentVoteCount, CurrentMi, TimerPID, ArbeitsZeit, Nameservice, KoordinatorName, MeinProzessName, Logfile)
  end.



%Diagramm 2 : 3.1
berechneMi(Mi, Y) -> 
  if Y < Mi ->
    MiNeu = ((Mi - 1) rem Y) + 1;
    true ->
      MiNeu = Mi
  end,
  MiNeu.
%Sleep

resetTimer(TimerPID) ->
  TimerPID ! resettimer.

setTimer(Pid, TimeMS, TimeoutReplyMsg) ->
  receive
    resettimer ->
      setTimer(Pid, TimeMS, TimeoutReplyMsg);
    kill ->
      true
  after
    TimeMS ->
      Pid ! TimeoutReplyMsg
  end,
  %notimeout / timeout
  setTimer(Pid, TimeMS, TimeoutReplyMsg). %Restart timer

initTimer(HalfTimeoutInMS) -> %INIT WITH timeout/2
  spawn(ggt, setTimer, [self(), HalfTimeoutInMS, timeoutMessage]).

%Flush current Queue. Diagramm 2 : 1.1
flush() -> flush(false).

flush(KillReceived) ->
  receive
    kill ->
      %Kill this process
      flush(true);
    _ ->
      flush(KillReceived)
  after
    1 -> %Mit 0 aufrufen > Ruft dadurch erst alle Messages ab und stellt sicher das Queue leer ist
      % Restart listening block
      case KillReceived of
        true ->
          self() ! kill;
        _ -> true
      end
  end.

%Ref: 4. : Beendigung
killMe(Nameservice, ProzessName, TimerPID) ->
  TimerPID ! kill,
  Nameservice ! {self(), {unbind, ProzessName}}, %4. ggt. a
  receive
    ok -> io:format("..unbind..done.\n")
  after
    10000 ->
      io:format("ATTENTION: ..Kein unbind ACK erhalten vom Nameservice. Terminiere nun trotzdem...\n")
  end,

  unregister(ProzessName).

	
	