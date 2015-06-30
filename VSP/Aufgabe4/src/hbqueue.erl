-module(hbqueue).
-import(werkzeug, [get_config_value/2,logging/2,logstop/0,openSe/2,openSeA/2,openRec/3,openRecA/3,createBinaryS/1,createBinaryD/1,createBinaryT/1,createBinaryNS/1,concatBinary/4,message_to_string/1,shuffle/1,timeMilliSecond/0,reset_timer/3,compareNow/2,getUTC/0,compareUTC/2,now2UTC/1,type_is/1,to_String/1,bestimme_mis/2,testeMI/2]).

%% API
-export([initHBQueue/0, push/2, getNextFreeSlot/1, isSlotFree/2]).

%% Schnittstellen

%% Inialisierung
% erzeugt eine neue, leere HoldbackQueue
initHBQueue() ->
  {0, {[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], []}}.


%% Getter
% Senden - 1.1: liefert einen zufälligen, noch freien Slot des nächsten Frames
getNextFreeSlot({_Frame, _MyStation, Received}) ->
  FreeSlotList = collectFreeSlots(Received),
  lists:nth(random:uniform(length(FreeSlotList)), FreeSlotList).

% Senden - 1.4: gibt true zurück, wenn im angegebenen Slot noch keine Nachricht ankam
isSlotFree({_Frame, _MyStation, Received}, Slot) ->
  element(Slot, Received) =:= null.


%% Inhaltserzeuger
% fügt die Nachricht zur Queue hinzu
push({MyFrame, MyStation, Received}, Msg) ->
  MsgTime = message:getTime(Msg),
  Frame = clock:getFrameByTime(MsgTime),

  if
    Frame =:= MyFrame ->
      Collision = checkCollision(Received, Msg),
      case Collision of
         true -> true
      end,

      NewReceived = addMessageToReceived(Received, Msg);
    true -> NewReceived = Received
  end,

  {MyFrame, NewReceived}.


%% interne Hilfsmethoden
% Empfang - alt: prüft, ob eine Nachricht eine Kollision verursacht
checkCollision(Hbq, Msg) ->
  true.

% Empfang - 3, 8: fügt die Nachricht zur internen Received-Queue hinzu
addMessageToReceived(Received, Msg) ->
  Slot =  clock:getSlotByTime(message:getTime(Msg)),
  OldMessages = element(Slot, Received),
  setelement(Slot, Received, OldMessages ++ [Msg]).

% Empfang - 5: speichert die im kommenden Frame reservierten Slots
updateReservedSlotsForComingFrame(Hbq) ->
  true.

% Empfang - 6: leert die Queue der empfangenen Nachrichten
clearReceivedMessages(Hbq) ->
  true.

% erstellt eine Liste mit noch freien Slots
collectFreeSlots(Received) ->
  collectFreeSlots(Received, 1).

collectFreeSlots(_Received, 26) ->
  [];

collectFreeSlots(Received, Nr) when (length(element(Nr, Received)) == 0) ->
  collectFreeSlots(Received, Nr+1) ++ [Nr];

collectFreeSlots(Received, Nr) ->
  collectFreeSlots(Received, Nr+1).

% Behandelt eine Kollision im angegebenen Slot, sofern eine Auftrat
handleCollision(Slot, Messages) when (length(element(Slot, Messages)) > 1) ->
  true.

handleCollision(Slot, Messages) -> true.