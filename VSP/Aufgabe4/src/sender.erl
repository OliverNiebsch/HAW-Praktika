-module(sender).
-import(werkzeug, [get_config_value/2,logging/2,logstop/0,openSe/2,openSeA/2,openRec/3,openRecA/3,createBinaryS/1,createBinaryD/1,createBinaryT/1,createBinaryNS/1,concatBinary/4,message_to_string/1,shuffle/1,timeMilliSecond/0,reset_timer/3,compareNow/2,getUTC/0,compareUTC/2,now2UTC/1,type_is/1,to_String/1,bestimme_mis/2,testeMI/2]).

%% API
-export([newSender/0, interruptSendTimer/1]).

%% Schnittstellen

%% Inialisierung
% erzeugt ein neues SendeModul
newSender() ->
  true.


%% Timer
% Empfang - 2: Stoppt den SendTimer des SendeModuls und startet den CollsionTimer
interruptSendTimer(Sender) ->
  setCollisionTimerUntilFrameEnds(Sender),
  true.


%% interne Hilfsmethoden
% Empfang - 2.1 | Senden - 5: startet den CollsionTimer des SendeModuls bis zum Ende des Frames
setCollisionTimerUntilFrameEnds(Sender) ->
  true.

% Senden(Kollision) - 1: Einstiegsmethode, wenn der CollisionTimer abgelaufen ist
collisionTimerEvent(Sender, Hbq, Clock) ->
  FreeSlot = hbqueue:getNextFreeSlot(Hbq),
  Data = datenquelle:getNextData(),

  Message = message:newMessage(Data),
  

  setSendTimer(clock:getTimespanToSlot(Clock, FreeSlot)),
  true. % return updated sender

% Senden(Kollision) - 1.8 | Senden - 4.1.4: startet den SendTimer für eine gegebene Zeitspanne
setSendTimer(Timespan) ->
  true.

% Senden - 1: Einstiegsmethode, wenn der SendTimer abgelaufen ist
sendTimerEvent(Sender, Hbq, Clock) ->
  FreeSlot = hbqueue:getNextFreeSlot(Hbq),
  message:setNextSlot(Message, FreeSlot),

  SlotIsFree = hbqueue:isSlotFree(Hbq, MySlot),
  CurrentTime = clock:getCurrentTimeInSlot(Clock, MySlot),

  if
    SlotIsFree =:= true, CurrentTime =/= null ->
      message:setTime(Message, CurrentTime),
      sendMessage(Message),

      Data = datenquelle:getNextData(),
      Message = message:newMessage(Data),
      setSendTimer(clock:getTimespanToSlot(Clock, FreeSlot)),
      true;  % return updated sender

    true ->
      setCollisionTimerUntilFrameEnds(Sender),
      true  % return updated sender?
  end.

% Senden - 3: schickt die Nachricht per Multicast raus
sendMessage(Message) ->
  true.
