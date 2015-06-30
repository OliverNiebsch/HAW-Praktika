-module(clock).
-import(werkzeug, [get_config_value/2,logging/2,logstop/0,openSe/2,openSeA/2,openRec/3,openRecA/3,createBinaryS/1,createBinaryD/1,createBinaryT/1,createBinaryNS/1,concatBinary/4,message_to_string/1,shuffle/1,timeMilliSecond/0,reset_timer/3,compareNow/2,getUTC/0,compareUTC/2,now2UTC/1,type_is/1,to_String/1,bestimme_mis/2,testeMI/2]).

%% API
-export([initClock/0, getTimespanToSlot/2, getCurFrame/1, getCurSlot/1, getCurrentTimeInSlot/2]).

%% Schnittstellen

%% Initialisierung
% erzeugt eine neue Clock
initClock(Offset) ->
  [Offset,PID_Receive].

%% Getter
% Senden(Kollision) - 1.6: liefert die Zeitspanne in Millisekunden bis zum angegebenen Slot
getTimespanToSlot(Clock, FreeSlot, Frame) ->
	(getCurrentTime - calculateTimeBySlot).

%Liefert den Zeitpunkt andem ein bestimmter Slot beginnt als Timestamp in MS
calculateTimeBySlot(Slot, Frame) ->
	((Slot-1) * 40) + Frame.
  
% liefert den aktuellen Frame
getCurFrame(Clock) ->
	getFrameByTime(getCurrentTime(Clock)).
	
% liefert den aktuellen Slot
getCurSlot(Clock) ->
  getSlotByTime(getCurrentTime(Clock)).

getCurrentTime(Clock) ->
	[Offset,_PID_Receive] = Clock,
	werkzeug:getUTC() + Offset.
  
% Senden - 1.6: liefert die aktuelle Zeit, wenn der angegebene Slot noch aktiv ist
getCurrentTimeInSlot(Clock, SlotNumber, Frame) ->
  CurFrame = getCurFrame(Clock),
  CurSlot = getCurSlot(Clock),
  if
    CurFrame == Frame and CurSlot == SlotNumber ->
		getCurrentTime(Clock);
	true -> 
	  null
  end.
  
getSlotByTime(Timestamp) ->
	Timestamp rem getFrameByTime(Timestamp) div 40.

getFrameByTime(Timestamp) ->
	Timestamp div 1000.

%Sync mit anderem Timestamp
synchronize(Clock, Timestamp) ->
	Differenz = Timestamp - (getCurrentTime(Clock)),
	NewOffset = Differenz/2,
	setOffset(Clock, NewOffset).

setOffset(Clock, NewOffset) ->
	[Offset,PID_Receive] = Clock,
	[NewOffset, PID_Receive].
	

	
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
  %setTimer(Pid, TimeMS, TimeoutReplyMsg). %Restart timer

initTimer(HalfTimeoutInMS) -> %INIT WITH timeout/2
  spawn(ggt, setTimer, [self(), HalfTimeoutInMS, timeoutMessage]).
