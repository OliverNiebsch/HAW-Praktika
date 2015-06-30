-module(message).

%% API
-export([packetToMessageObj/1, newMessage/5, getFrame/1, getStation/1, getStationTyp/1, getTime/1,  setNextSlot/2, setTime/2]).

%% Schnittstellen

%% Initialisierung
% Empfang - 1.1: erzeugt ein Message-Objekt aus bin�ren Daten
packetToMessageObj(Packet) ->
	{StationTyp,Nutzdaten,Slot,Timestamp} = werkzeug:message_to_string(Packet),
    {-1, StationTyp,Nutzdaten,Slot,Timestamp}. %Frame nummer auf -1 weil bei empfangenen Messages irrelevant.

  
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
  {_TargetFrame, _StationTyp,Nutzdaten,_Slot,_Timestamp} = Message,
	%Nutzdaten 1-10 = Name der Station
	%{Stationsname, _} = lists:split(10, Nutzdaten), %Alternative Variante
	Stationsname = lists:sublist(Nutzdaten, 10),
	Stationsname.
	

getStationTyp(Message) ->
  {_TargetFrame, StationTyp,_Nutzdaten,_Slot,_Timestamp} = Message,
	StationTyp.
	
getTime(Message) ->
	{TargetFrame, StationTyp,Nutzdaten,Slot,Timestamp},
	Timestamp.
	
%% Setter
% Senden - 1.3: setzen des Slots, in dem die Station dieser Nachricht im n�chsten Frame senden will
setNextSlot(Message, Slot, TargetFrame) ->
	{_, StationTyp, Nutzdaten, _, Timestamp} = Message,
	{TargetFrame, StationTyp, Nutzdaten,  Slot, Timestamp}.

% Senden - 2: setzt die Sendezeit der Nachricht
setTime(Message, Time) ->
	{TargetFrame, StationTyp,Nutzdaten,Slot, _} = Message,
	{TargetFrame, StationTyp, Nutzdaten, Slot, Time}.
	
messageToBinary(Message) ->
	{TargetFrame, StationTyp, Nutzdaten, Slot, Timestamp} = Message,
	concatBinary(createBinaryS(StationTyp), createBinaryD(Nutzdaten), createBinaryNS(Slot), createBinaryT(Timestamp)).