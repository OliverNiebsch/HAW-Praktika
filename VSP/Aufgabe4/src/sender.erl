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
  ZielAddr = {142,22,78,197}, %{225,10,1,2},
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
  {Adapter, FreeSlot, SendMsg} = checkSlot(Sender, Hbq, Frame),

  Data = datenquelle:getNextData(),
  Message = message:setData(SendMsg, Data),

  % SendTimer starten
  clock:startSendTimer(Clock, FreeSlot, clock:getCurFrame(Clock)),
  {Adapter, FreeSlot, Message}. % return updated sender

% Senden - 1: Einstiegsmethode, wenn der SendTimer abgelaufen ist
send({Adapter, MySlot, Message}, Hbq, Clock) ->
  FreeSlot = hbqueue:getNextFreeSlot(Hbq),
  Frame = message:getFrame(Message),
  message:setNextSlot(Message, FreeSlot),

  SlotIsFree = hbqueue:isSlotFree(Hbq, MySlot),
  CurrentTime = clock:getCurrentTimeInSlot(Clock, MySlot, Frame),

  if
    (SlotIsFree =:= true) and (CurrentTime =/= null) ->
      message:setTime(Message, CurrentTime),
      sendMessage(Adapter, Message),

      MessageNeu = message:newMessage(message:getStation(Message), null),
      {Adapter, FreeSlot, message:setFrame(MessageNeu, Frame + 1)};  % return updated sender

    true ->
      {Adapter, null, message:newMessage(message:getStation(Message), null)}  % return updated sender?
  end.

%% interne Hilfsmethoden
% Senden - 3: schickt die Nachricht per Multicast raus
sendMessage(SendAdapter, Message) ->
  {Socket, ZielAddr, Port} = SendAdapter,
  gen_udp:send(Socket, ZielAddr, Port, Message).

checkSlot({Adapter, null, Msg}, Hbq, Frame) ->
  NewMsg = message:setFrame(Msg, Frame),
  NewSlot = hbqueue:getNextFreeSlot(Hbq),
  {Adapter, NewSlot, NewMsg};

checkSlot(Sender, _Hbq, _Frame) -> Sender.
