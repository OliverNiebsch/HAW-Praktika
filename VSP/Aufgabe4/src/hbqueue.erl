-module(hbqueue).
-import(werkzeug, [get_config_value/2, logging/2, logstop/0, openSe/2, openSeA/2, openRec/3, openRecA/3, createBinaryS/1, createBinaryD/1, createBinaryT/1, createBinaryNS/1, concatBinary/4, message_to_string/1, shuffle/1, timeMilliSecond/0, reset_timer/3, compareNow/2, getUTC/0, compareUTC/2, now2UTC/1, type_is/1, to_String/1, bestimme_mis/2, testeMI/2]).

%% API
-export([initHBQueue/0, push/2, resetHBQForNewFrame/2, getNextFreeSlot/1, isSlotFree/2]).

%% Schnittstellen

%% Inialisierung
% erzeugt eine neue, leere HoldbackQueue
initHBQueue() ->
  {0, {[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], []}}.


%% Getter
% Senden - 1.1: liefert einen zufaelligen, noch freien Slot des naechsten Frames
getNextFreeSlot({_Frame, _MyStation, Received}) ->
  FreeSlotList = collectFreeSlots(Received),
  lists:nth(random:uniform(length(FreeSlotList)), FreeSlotList).

% Senden - 1.4: gibt true zurueck, wenn im angegebenen Slot noch keine Nachricht ankam
isSlotFree({_Frame, _MyStation, Received}, Slot) ->
  element(Slot, Received) =:= null.


%% Inhaltserzeuger
% fuegt die Nachricht zur Queue hinzu
push({MyFrame, MyStation, Received}, Msg) ->
  MsgTime = message:getTime(Msg),
  Frame = clock:getFrameByTime(MsgTime),

  if
    Frame =:= MyFrame ->
      Slot = clock:getSlotByTime(message:getTime(Msg)),
      NewReceived = addMessageToReceived(Slot, Received, Msg),

      Collision = checkCollision(Slot, Received, MyStation),
      {Collision, {MyFrame, NewReceived}};

    true ->
      Collision = false,
      NewReceived = Received
  end,

  {Collision, {MyFrame, MyStation, NewReceived}}.

% Empfang - 6: Resettet die HBQ fuer einen neuen Frame
resetHBQForNewFrame({_OldFrame, Messages}, NewFrame) ->
  MsgList = getCollisionFreeMessages(Messages),
  datensenke:printAllMessages(MsgList),
  {NewFrame, {[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], []}}.


%% interne Hilfsmethoden
% liefert eine Liste mit allen Nachrichten, die nicht in Kollisionen verwickelt ware
getCollisionFreeMessages(Messages) ->
  getCollisionFreeMessages(Messages, 1).

getCollisionFreeMessages(_Messages, 26) -> [];

getCollisionFreeMessages(Messages, N) when (length(element(N, Messages)) == 1) ->
  Elem = element(N, Messages),
  getCollisionFreeMessages(Messages, N + 1) ++ Elem;

getCollisionFreeMessages(Messages, N) -> getCollisionFreeMessages(Messages, N + 1).

% Empfang - alt: prueft, ob eine Nachricht eine Kollision verursacht
checkCollision(Slot, Messages, MyStation) when (length(element(Slot, Messages)) > 1) ->
  % TODO: logging
  msgOfMyStation(Messages, MyStation) =:= true;

checkCollision(_Slot, _Messages, _MyStation) -> false.

% Empfang - 3, 8: fuegt die Nachricht zur internen Received-Queue hinzu
addMessageToReceived(Slot, Received, Msg) ->
  OldMessages = element(Slot, Received),
  setelement(Slot, Received, OldMessages ++ [Msg]).

% erstellt eine Liste mit noch freien Slots
collectFreeSlots(Received) ->
  collectFreeSlots(Received, 1).

collectFreeSlots(_Received, 26) ->
  [];

collectFreeSlots(Received, Nr) when (length(element(Nr, Received)) == 0) ->
  collectFreeSlots(Received, Nr + 1) ++ [Nr];

collectFreeSlots(Received, Nr) ->
  collectFreeSlots(Received, Nr + 1).

% prueft, ob die MyStation die Station einer Nachricht in der Liste ist
msgOfMyStation([], _MyStation) ->
  false;

msgOfMyStation([Msg, Messages], MyStation) ->
  Station = message:getStation(Msg),
  if
     Station =:= MyStation ->
      true;
    true ->
      msgOfMyStation(Messages, MyStation)
  end.