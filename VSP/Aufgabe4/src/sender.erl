-module(sender).
-import(werkzeug, [get_config_value/2,logging/2,logstop/0,openSe/2,openSeA/2,openRec/3,openRecA/3,createBinaryS/1,createBinaryD/1,createBinaryT/1,createBinaryNS/1,concatBinary/4,message_to_string/1,shuffle/1,timeMilliSecond/0,reset_timer/3,compareNow/2,getUTC/0,compareUTC/2,now2UTC/1,type_is/1,to_String/1,bestimme_mis/2,testeMI/2]).

%% API
-export([newSender/2, resetSendSlot/1, frameStarts/4, send/3]).

%% Schnittstellen

%% Inialisierung
% erzeugt ein neues SendeModul
newSender(StationTyp, WadisMeeep) ->
  %openSe(IP,Port) -> Socket % diesen Prozess PidSend (als Nebenlaeufigenprozess gestartet) bekannt geben mit
  Teamnummer =  8,
  ZielAddr = {225,10,1,2},
  LocalAdress = WadisMeeep,
  Port = 15000 + Teamnummer,

  Socket = openSe(LocalAdress, Port),
  gen_udp:controlling_process(Socket, self()), %Anmelden als handler fuer Socket

  % SendSlot, Message(StationTyp, Data)
  {{Socket, ZielAddr, Port}, null, message:newMessage(StationTyp, null)}.


%% Inhaltserzeugung
% setzt den Slot, in dem das SendeModul senden will, auf null (nach Kollision)
resetSendSlot({Adapter, _Slot, Msg}) ->
  {Adapter, null, Msg}.

% FrameTimer - 1.3: Einstiegsmethode, wenn der FrameTimer abgelaufen ist
frameStarts(Frame, Sender, Hbq, Clock) ->
  logging("sender.log", "Sender: Neuer Frame hat begonnen\n"),
  {Adapter, FreeSlot, SendMsg} = checkSlot(Sender, Hbq, Frame),

  Data = datenquelle:getNextData(),
  Message = message:setData(SendMsg, Data),
  logging("sender.log", "Sender: Nachricht mit Daten gefuellt \"" ++ to_String(Data) ++ "\"\n"),

  % SendTimer starten
  clock:startSendTimer(Clock, FreeSlot, clock:getCurFrame(Clock)),
  {Adapter, FreeSlot, Message}. % return updated sender

% Senden - 1: Einstiegsmethode, wenn der SendTimer abgelaufen ist
send({Adapter, MySlot, Message}, Hbq, Clock) ->
  logging("sender.log", "Sender: Soll Nachricht senden.\n"),
  FreeSlot = hbqueue:getNextFreeSlot(Hbq),
  Frame = message:getFrame(Message),
  Message2 = message:setNextSlot(Message, FreeSlot),

  SlotIsFree = hbqueue:isSlotFree(Hbq, MySlot),
  CurrentTime = clock:getCurrentTimeInSlot(Clock, MySlot, Frame),

  if
    (SlotIsFree =:= true) and (CurrentTime =/= null) ->
      logging("sender.log", "Sender: Alles gut, versuche Nachricht zu senden.\n"),
      SendMessage = message:setTime(Message2, CurrentTime),
      sendMessage(Adapter, SendMessage),

      MessageNeu = message:newMessage(message:getStation(SendMessage), null),
      {Adapter, FreeSlot, message:setFrame(MessageNeu, Frame + 1)};  % return updated sender

    true ->
      logging("sender.log", "Sender: Nichts gut. SlotIsFree:" ++ to_String(SlotIsFree) ++ " - CurrentTime: " ++ to_String(CurrentTime) ++ "\n"),
      {Adapter, null, message:newMessage(message:getStation(Message2), null)}
  end.

%% interne Hilfsmethoden
% Senden - 3: schickt die Nachricht per Multicast raus
sendMessage(SendAdapter, Message) ->
  {Socket, ZielAddr, Port} = SendAdapter,
  BinMsg = message:messageToBinary(Message),
  gen_udp:send(Socket, ZielAddr, Port, BinMsg),
  logging("sender.log", "Sender: Nachricht gesendet\n").

checkSlot({Adapter, null, Msg}, Hbq, Frame) ->
  NewMsg = message:setFrame(Msg, Frame),
  NewSlot = hbqueue:getNextFreeSlot(Hbq),

  logging("sender.log", "Sender: Hatte noch keinen Slot. Slot " ++ to_String(NewSlot) ++ " erhalten\n"),
  {Adapter, NewSlot, NewMsg};

checkSlot(Sender, _Hbq, _Frame) -> Sender.
