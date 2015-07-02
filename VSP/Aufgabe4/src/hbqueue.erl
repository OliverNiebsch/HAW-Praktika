-module(hbqueue).
-import(werkzeug, [get_config_value/2, logging/2, logstop/0, openSe/2, openSeA/2, openRec/3, openRecA/3, createBinaryS/1, createBinaryD/1, createBinaryT/1, createBinaryNS/1, concatBinary/4, message_to_string/1, shuffle/1, timeMilliSecond/0, reset_timer/3, compareNow/2, getUTC/0, compareUTC/2, now2UTC/1, type_is/1, to_String/1, bestimme_mis/2, testeMI/2]).

%% API
-export([initHBQueue/1, push/2, resetHBQForNewFrame/2, getNextFreeSlot/1, isSlotFree/2]).

-define(LOGFILE, "logfile.log").

%% Schnittstellen

%% Inialisierung
% erzeugt eine neue, leere HoldbackQueue
initHBQueue(StationNr) ->
  {0, StationNr, {[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], []}}.


%% Getter
% Senden - 1.1: liefert einen zufaelligen, noch freien Slot des naechsten Frames
getNextFreeSlot({_Frame, _MyStation, Received}) ->
  FreeSlotList = collectFreeSlots(Received),
  %logging(?LOGFILE, "HBQ: Noch freie Slots = " ++ to_String(FreeSlotList) ++ "\n"),
  lists:nth(crypto:rand_uniform(0, length(FreeSlotList)) + 1, FreeSlotList).

% Senden - 1.4: gibt true zurueck, wenn im angegebenen Slot noch keine Nachricht ankam
isSlotFree({_Frame, _MyStation, Received}, Slot) ->
  length(element(Slot, Received)) =:= 0.


%% Inhaltserzeuger
% fuegt die Nachricht zur Queue hinzu
push({MyFrame, MyStation, Received}, Msg) ->
  %logging(?LOGFILE, "HBQ: Neue Nachricht erhalten.\n"),
  MsgTime = message:getTime(Msg),
  Frame = clock:getFrameByTime(MsgTime),

  if
    Frame =:= MyFrame ->
      Slot = clock:getSlotByTime(MsgTime),
      NewReceived = addMessageToReceived(Slot, Received, Msg),

      Collision = checkCollision(Slot, MyStation, NewReceived),
      {Collision, {MyFrame, NewReceived}};

    true ->
      %logging(?LOGFILE, "HBQ: Nachricht ist aus falschem Frame.\n"),
      Collision = false,
      NewReceived = Received
  end,

  {Collision, {MyFrame, MyStation, NewReceived}}.

% Empfang - 6: Resettet die HBQ fuer einen neuen Frame
resetHBQForNewFrame({_OldFrame, MyStation, Messages}, NewFrame) ->
  %logging(?LOGFILE, "HBQ wird geleert\n"),
  MsgList = getCollisionFreeMessages(Messages),
  datensenke:printAllMessages(MsgList),
  {NewFrame, MyStation, {[], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], [], []}}.


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
checkCollision(Slot, MyStation, Messages) when (length(element(Slot, Messages)) > 1) ->
  %logging(?LOGFILE, "HBQ: Collision festgestellt.\n"),
  msgOfMyStation(element(Slot, Messages), MyStation) =:= true;

checkCollision(_Slot, _Messages, _MyStation) -> false.

% Empfang - 3, 8: fuegt die Nachricht zur internen Received-Queue hinzu
addMessageToReceived(Slot, Received, Msg) ->
  OldMessages = element(Slot, Received),
  setelement(Slot, Received, OldMessages ++ [Msg]).

% erstellt eine Liste mit noch freien Slots
collectFreeSlots(Received) ->
  collectFreeSlots(Received, 1, [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25]).

collectFreeSlots(_Received, 26, FreeSlotList) ->
  FreeSlotList;

collectFreeSlots(Received, Nr, FreeSlotList) when (length(element(Nr, Received)) == 1) ->
  [Msg] = element(Nr, Received),
  collectFreeSlots(Received, Nr + 1, lists:delete(message:getReservedSlot(Msg), FreeSlotList));

collectFreeSlots(Received, Nr, FreeSlotList) ->
  collectFreeSlots(Received, Nr + 1, FreeSlotList).

% prueft, ob die MyStation die Station einer Nachricht in der Liste ist
msgOfMyStation([], _MyStation) ->
  false;

msgOfMyStation([Msg | Messages], MyStation) ->
  Station = message:getStation(Msg),
  if
     Station =:= MyStation ->
       %logging(?LOGFILE, "HBQ: Nachricht von eigener Station war in Kollision beteiligt.\n"),
       true;
    true ->
      msgOfMyStation(Messages, MyStation)
  end.