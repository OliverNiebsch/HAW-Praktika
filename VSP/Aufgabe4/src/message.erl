-module(message).

%% API
-export([binaryToMessageObj/1, newMessage/1, getFrame/1, getStation/1, setNextSlot/2, setTime/2]).

%% Schnittstellen

%% Initialisierung
% Empfang - 1.1: erzeugt ein Message-Objekt aus binären Daten
binaryToMessageObj(binaryMessage) ->
  true.

% Senden(Kollision) - 1.5 | Senden - 4.1.1: erzeugt ein leeres Message-Objekt mit den angegebene Daten
newMessage(TargetFrame, StationTyp, Nutzdaten, Slot, Timestamp) ->
  {TargetFrame, StationTyp,Nutzdaten,Slot,Timestamp}.
  
%% Getter
% Empfang - 1.2.1: gibt den Frame, in der die Message gesendet werden soll bzw gesandt wurde an
getFrame(Message) ->
	{TargetFrame, _StationTyp,_Nutzdaten,_Slot,_Timestamp} = Message,
	TargetFrame.

% Empfang - alt: gibt die Station an, von der die Message gesendet wurde/wird
getStation(Message) ->
  {_TargetFrame, StationTyp,_Nutzdaten,_Slot,_Timestamp} = Message,
	StationTyp.

%% Setter
% Senden - 1.3: setzen des Slots, in dem die Station dieser Nachricht im nächsten Frame senden will
setNextSlot(Message, Slot) ->
	{TargetFrame, StationTyp, Nutzdaten, _, Timestamp} = Message,
	{TargetFrame, StationTyp, Nutzdaten,  Slot, Timestamp}.

% Senden - 2: setzt die Sendezeit der Nachricht
setTime(Message, Time) ->
	{TargetFrame, StationTyp,Nutzdaten,Slot, _} = Message,
	{TargetFrame, StationTyp, Nutzdaten, Slot, Time}.

	
	
%% interne Hilfsmethoden
% Nachrichtenpaket fertig stellen
createBinaryS(Station) ->
    % 1 Byte for Stationtype  
%	<<(list_to_binary(Station)):8/binary>>.
	<<(list_to_binary(Station))/binary>>.
createBinaryD(Data) ->
    % 24 Byte for Payload  
%	<<(list_to_binary(Data)):192/binary>>.
	<<(list_to_binary(Data))/binary>>.
createBinaryNS(NextSlot) ->
    % 1 Byte for NextSlot
%    <<NextSlot:8/integer>>.
    <<NextSlot>>.
createBinaryT(Timestamp) ->    
    % 8 Byte for Time  
    <<(Timestamp):64/big-unsigned-integer>>.	
concatBinary(BinStation,BinData,BinNextSlot,BinTime) ->         
    % Konkatenieren der Binaries: Nachrichtenformat pruefen!             
    <<BinStation/binary, BinData/binary,BinNextSlot/binary,BinTime/binary>>.
