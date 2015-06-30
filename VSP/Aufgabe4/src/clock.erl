-module(clock).
-import(werkzeug, [get_config_value/2,logging/2,logstop/0,openSe/2,openSeA/2,openRec/3,openRecA/3,createBinaryS/1,createBinaryD/1,createBinaryT/1,createBinaryNS/1,concatBinary/4,message_to_string/1,shuffle/1,timeMilliSecond/0,reset_timer/3,compareNow/2,getUTC/0,compareUTC/2,now2UTC/1,type_is/1,to_String/1,bestimme_mis/2,testeMI/2]).

%% API
-export([newClock/0, getTimespanToSlot/2, getCurFrame/1, getCurSlot/1, getCurrentTimeInSlot/2]).

%% Schnittstellen

%% Inialisierung
% erzeugt eine neue Clock
newClock() ->
  true.


%% Getter
% Senden(Kollision) - 1.6: liefert die Zeitspanne in Millisekunden bis zum angegebenen Slot
getTimespanToSlot(clock, freeSlot) ->
  true.

% liefert den aktuellen Frame
getCurFrame(clock) ->
  true.

% liefert den aktuellen Slot
getCurSlot(clock) ->
  true.

% Senden - 1.6: liefert die aktuelle Zeit, wenn der angegebene Slot noch aktiv ist
getCurrentTimeInSlot(clock, slotNumber) ->
  true.
  
  
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
