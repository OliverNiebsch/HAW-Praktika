-module(hbqueue).
-import(werkzeug, [get_config_value/2,logging/2,logstop/0,openSe/2,openSeA/2,openRec/3,openRecA/3,createBinaryS/1,createBinaryD/1,createBinaryT/1,createBinaryNS/1,concatBinary/4,message_to_string/1,shuffle/1,timeMilliSecond/0,reset_timer/3,compareNow/2,getUTC/0,compareUTC/2,now2UTC/1,type_is/1,to_String/1,bestimme_mis/2,testeMI/2]).

%% API
-export([newHBQueue/0, push/2, getNextFreeSlot/1, isSlotFree/2]).

%% Schnittstellen

%% Inialisierung
% erzeugt eine neue, leere HoldbackQueue
newHBQueue() ->
  true.


%% Getter
% Senden - 1.1: liefert einen zufälligen, noch freien Slot des nächsten Frames
getNextFreeSlot(hbq) ->
  true.

% Senden - 1.4: gibt true zurück, wenn im angegebenen Slot noch keine Nachricht ankam
isSlotFree(hbq, slot) ->
  true.


%% Inhaltserzeuger
% fügt die Nachricht ans Ende der Queue hinzu
push(hbq, msg) ->
  true.


%% interne Hilfsmethoden
% Empfang - alt: prüft, ob eine Nachricht eine Kollision verursacht
checkCollision(hbq, msg) ->
  true.

% Empfang - 3, 8: fügt die Nachricht zur internen Received-Queue hinzu
addMessageToReceived(hbq, msg) ->
  true.

% Empfang - 5: speichert die im kommenden Frame reservierten Slots
updateReservedSlotsForComingFrame(hbq) ->
  true.

% Empfang - 6: leert die Queue der empfangenen Nachrichten
clearReceivedMessages(hbq) ->
  true.