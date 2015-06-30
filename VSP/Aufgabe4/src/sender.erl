-module(sender).
-import(werkzeug, [get_config_value/2,logging/2,logstop/0,openSe/2,openSeA/2,openRec/3,openRecA/3,createBinaryS/1,createBinaryD/1,createBinaryT/1,createBinaryNS/1,concatBinary/4,message_to_string/1,shuffle/1,timeMilliSecond/0,reset_timer/3,compareNow/2,getUTC/0,compareUTC/2,now2UTC/1,type_is/1,to_String/1,bestimme_mis/2,testeMI/2]).

%% API
-export([newSender/1, resetSendSlot/1, frameStarts/3, send/3]).

%% Schnittstellen

%% Inialisierung
% erzeugt ein neues SendeModul
newSender(StationTyp) ->
  %openSe(IP,Port) -> Socket % diesen Prozess PidSend (als Nebenläufigenprozess gestartet) bekannt geben mit
  Teamnummer =  8,
  ZielAddr = {225,10,1,2},
  LocalAdress = whatismyip,
  Port = 15000 + Teamnummer,

  Socket = openSe(LocalAdress, Port),
  gen_udp:controlling_process(Socket, self()), %Anmelden als handler für Socket

  % SendSlot, Message(StationTyp, Data)
  {{Socket, ZielAddr, Port}, null, message:newMessage(StationTyp, null)}.


%% Setter
% setzt den Slot, in dem das SendeModul senden will, auf null (nach Kollision)
resetSendSlot({Adapter, _Slot, Msg}) ->
  {Adapter, null, Msg}.


%% interne Hilfsmethoden
% Empfang - 2.1 | Senden - 5: startet den CollsionTimer des SendeModuls bis zum Ende des Frames
setCollisionTimerUntilFrameEnds(Sender) ->
  true.

% Senden(Kollision) - 1: Einstiegsmethode, wenn der FrameTimer abgelaufen ist
frameStarts({Adapter, SendSlot, SendMsg}, Hbq, Clock) when (SendSlot =:= null) ->
  % nach Kollision (bzw Anfang)
  FreeSlot = hbqueue:getNextFreeSlot(Hbq),
  Data = datenquelle:getNextData(),

  Message = message:setData(SendMsg, Data),
  MessageNeu = message:setNextSlot(Message, FreeSlot),

  % TODO: SendTimer starten
  {Adapter, FreeSlot, MessageNeu}; % return updated sender

frameStarts({Adapter, SendSlot, SendMsg}, _Hbq, Clock) ->
  % keine Kollision, nur Frame neu angefangen
  Data = datenquelle:getNextData(),
  MessageNeu = message:setData(SendMsg, Data),

  % TODO: SendTimer starten
  {Adapter, SendSlot, MessageNeu}.

% Senden - 1: Einstiegsmethode, wenn der SendTimer abgelaufen ist
send({Adapter, MySlot, Message}, Hbq, Clock) ->
  FreeSlot = hbqueue:getNextFreeSlot(Hbq),
  message:setNextSlot(Message, FreeSlot),

  SlotIsFree = hbqueue:isSlotFree(Hbq, MySlot),
  CurrentTime = clock:getCurrentTimeInSlot(Clock, MySlot),

  if
    SlotIsFree =:= true and CurrentTime =/= null ->
      message:setTime(Message, CurrentTime),
      sendMessage(Adapter, Message),

      Frame = message:getFrame(Message),
      MessageNeu = message:newMessage(message:getStation(Message), null),
      {Adapter, FreeSlot, message:setFrame(MessageNeu, Frame + 1)};  % return updated sender

    true ->
      {Adapter, null, message:newMessage(message:getStation(Message), null)}  % return updated sender?
  end.

% Senden - 3: schickt die Nachricht per Multicast raus
sendMessage(SendAdapter, Message) ->
  {Socket, ZielAddr, Port} = SendAdapter,
  gen_udp:send(Socket, ZielAddr, Port, Message).
