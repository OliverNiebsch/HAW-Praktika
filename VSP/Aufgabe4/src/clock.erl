-module(clock).
-import(werkzeug, [get_config_value/2, logging/2, logstop/0, openSe/2, openSeA/2, openRec/3, openRecA/3, createBinaryS/1, createBinaryD/1, createBinaryT/1, createBinaryNS/1, concatBinary/4, message_to_string/1, shuffle/1, timeMilliSecond/0, reset_timer/3, compareNow/2, getUTC/0, compareUTC/2, now2UTC/1, type_is/1, to_String/1, bestimme_mis/2, testeMI/2]).

%% API
-export([initClock/2, startSendTimer/3, startFrameTimer/1, getFrameByTime/1, getCurFrame/1, getCurSlot/1, getCurrentTimeInSlot/3, synchronize/2, setTimer/3, resetSendTimer/1, getSlotByTime/1]).

-define(LOGFILE, "logfile.log").

%% Schnittstellen

%% Initialisierung
% erzeugt eine neue Clock
initClock(Offset, PID_Receive) ->
  Clock = {Offset, PID_Receive, nil, nil},

  ClockNeu = startFrameTimer(Clock),

  ClockNeu.

startSendTimer(Clock, Slot, Frame) when (Slot > 0) and (Slot < 26) ->
  % TODO Alten Timer killen
  {Offset, PID_Receive, FrameTimerPID, _} = Clock,

  Timeout = getTimespanToSlot(Clock, Slot, Frame),
  SendTimerPID = initTimer(Timeout + 19, PID_Receive, sendTimer),
  %logging(?LOGFILE, "Clock: SendTimer fuer Slot " ++ to_String(Slot) ++ " gestartet.\n"),

  {Offset, PID_Receive, FrameTimerPID, {SendTimerPID, Slot, Frame}}.

startFrameTimer(Clock) ->
  % TODO Alten Timer killen
  {Offset, PID_Receive, _, SendTimer} = Clock,
  CurrentTime = getCurrentTime(Clock),

  Timeout = 1000 - (CurrentTime rem 1000),
  FrameTimerPID = initTimer(Timeout + 10, PID_Receive, frameTimer),
  %logging(?LOGFILE, "Clock: FrameTimer gestartet.\n"),

  {Offset, PID_Receive, FrameTimerPID, SendTimer}.

setTimer(Pid, TimeMS, _TimeoutReplyMsg) when (is_integer(TimeMS) =:= false) ->
  logging(?LOGFILE, "Clock Timer: setTimer mit falschem Timeout: " ++ to_String(TimeMS) ++ "\n"),
  Pid ! kill;

setTimer(Pid, TimeMS, TimeoutReplyMsg) ->
  %logging(?LOGFILE, "Clock: Timer gestartet mit Timeout: " ++ to_String(TimeMS) ++ "\n"),
  receive
    {sync, Timeout} when Timeout > 0 ->
      %logging(?LOGFILE, "Clock: Timer received: sync Timeout >0 :\n"),
      setTimer(Pid, Timeout, TimeoutReplyMsg);
    {sync, _Timeout} ->
      %logging(?LOGFILE, "Clock: Timer received: sync Timeout >0 :\n"),
      Pid ! TimeoutReplyMsg;
    kill ->
      true
  after
    TimeMS ->
      %logging(?LOGFILE, "Clock: Timer hat Timeout erreicht. Sende Nachricht: " ++ to_String(TimeoutReplyMsg) ++ "\n"),
      Pid ! TimeoutReplyMsg
  end.
%notimeout / timeout
%setTimer(Pid, TimeMS, TimeoutReplyMsg). %Restart timer

initTimer(Timeout, PID_Receive, Message) ->
  spawn(clock, setTimer, [PID_Receive, Timeout, Message]).

%% Getter
% Senden(Kollision) - 1.6: liefert die Zeitspanne in Millisekunden bis zum angegebenen Slot
getTimespanToSlot(Clock, FreeSlot, Frame) ->
  calculateTimeBySlot(FreeSlot, Frame) - getCurrentTime(Clock).

%Liefert den Zeitpunkt andem ein bestimmter Slot beginnt als Timestamp in MS
calculateTimeBySlot(Slot, Frame) ->
  Time = ((Slot - 1) * 40) + Frame * 1000,
  %logging(?LOGFILE, "Clock: Slot " ++ to_String(Slot) ++ " in Frame " ++ to_String(Frame) ++ " faengt an um " ++ to_String(Time) ++ ".\n"),
  Time.

% liefert den aktuellen Frame
getCurFrame(Clock) ->
  getFrameByTime(getCurrentTime(Clock)).

% liefert den aktuellen Slot
getCurSlot(Clock) ->
  getSlotByTime(getCurrentTime(Clock)).

getCurrentTime(Clock) ->
  {Offset, _PID_Receive, _FrameTimerPID, _SendTimerPID} = Clock,
  Time = werkzeug:getUTC() + Offset,
  %logging(?LOGFILE, "Clock: Uhrzeit ist " ++ to_String(Time) ++ ".\n"),
  Time.

% Senden - 1.6: liefert die aktuelle Zeit, wenn der angegebene Slot noch aktiv ist
getCurrentTimeInSlot(Clock, SlotNumber, Frame) ->
  CurFrame = getCurFrame(Clock),
  CurSlot = getCurSlot(Clock),

  %logging(?LOGFILE, "Clock: Anfrage nach Zeit in " ++ to_String(Frame) ++ "-" ++ to_String(SlotNumber) ++ ". Aktuell sind wir in " ++ to_String(CurFrame) ++ "-" ++ to_String(CurSlot) ++ "\n"),

  if
    (CurFrame =:= Frame) and (CurSlot =:= SlotNumber) ->
      getCurrentTime(Clock);
    true ->
      null
  end.

getSlotByTime(Timestamp) ->
  ((Timestamp rem 1000) div 40) + 1.

getFrameByTime(Timestamp) ->
  Timestamp div 1000.

%Sync mit anderem Timestamp
synchronize(Clock, Timestamp) ->
  {Offset, _PID_Receive, _FrameTimerPID, _SendTimerPID} = Clock,

  Differenz = Timestamp - (getCurrentTime(Clock)),

  %logging(?LOGFILE, "Clock: Synchronize mit Differenz von " ++ to_String(Differenz) ++ "\n"),

  HalfDiff = (Differenz div 2),
  NewOffset = Offset + HalfDiff, %Floor - dif = 1  === ?
  setTimerSyncDrift(Clock, HalfDiff), % PUSH MSG TO TIMERS
  setOffset(Clock, NewOffset).

setTimerSyncDrift({_Offset, _PID_Receive, FrameTimerPID, nil}, OffsetDiff) ->
  Clock = {_Offset, _PID_Receive, FrameTimerPID, nil},
  CurrentTime = getCurrentTime(Clock),

  NewFrameTimeout = 1000 - (CurrentTime rem 1000) - OffsetDiff, %WIRD NEGATIV; WENN FRAME GRENEZ DURCH ANPASSUNG ÜBERSPRUNGEN
  FrameTimerPID ! {sync, NewFrameTimeout};

setTimerSyncDrift(Clock, OffsetDiff) ->
  %beide Timer syncen
  {_Offset, _PID_Receive, FrameTimerPID, {SendTimerPID, Slot, Frame}} = Clock,
  CurrentTime = getCurrentTime(Clock),

  NewFrameTimeout = 1000 - (CurrentTime rem 1000) - OffsetDiff, %WIRD NEGATIV; WENN FRAME GRENEZ DURCH ANPASSUNG ÜBERSPRUNGEN
  FrameTimerPID ! {sync, NewFrameTimeout},

  NewSendTimeout = getTimespanToSlot(Clock, Slot, Frame) + 19,
  SendTimerPID ! {sync, NewSendTimeout}.

setOffset(Clock, NewOffset) ->
  {_Offset, PID_Receive, FrameTimerPID, SendTimer} = Clock,
  {NewOffset, PID_Receive, FrameTimerPID, SendTimer}.

resetSendTimer({Offset, PID_Receive, FrameTimerPID, _SendTimer}) ->
  {Offset, PID_Receive, FrameTimerPID, nil}.