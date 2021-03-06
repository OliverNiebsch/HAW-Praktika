-module(message).
-import(werkzeug, [get_config_value/2, logging/2, logstop/0, openSe/2, openSeA/2, openRec/3, openRecA/3, createBinaryS/1, createBinaryD/1, createBinaryT/1, createBinaryNS/1, concatBinary/4, message_to_string/1, shuffle/1, timeMilliSecond/0, reset_timer/3, compareNow/2, getUTC/0, compareUTC/2, now2UTC/1, type_is/1, to_String/1, bestimme_mis/2, testeMI/2]).

%% API
-export([packetToMessageObj/1, newMessage/2, getFrame/1, getStation/1, getData/1, getStationTyp/1, getTime/1, getReservedSlot/1, setNextSlot/2, setTime/2, setFrame/2, setData/2, messageToBinary/1]).

-define(LOGFILE, "logfile.log").

%% Schnittstellen

%% Initialisierung
% Empfang - 1.1: erzeugt ein Message-Objekt aus binaeren Daten
packetToMessageObj(Packet) ->
  {StationTyp, Nutzdaten, Slot, Timestamp} = binary_to_message(Packet),

  %logging(?LOGFILE, "Erstelle Message aus Binray um, mit Daten: " ++ to_String(StationTyp) ++ ", " ++ to_String(Nutzdaten) ++ ", " ++ to_String(Slot) ++ ", " ++ to_String(Timestamp) ++ "\n"),
  {-1, StationTyp, Nutzdaten, Slot, Timestamp}. %Frame nummer auf -1 weil bei empfangenen Messages irrelevant.

% helper
binary_to_message(Packet)	->
%	Packet= <<BinStationTyp:8/binary,BinNutzdaten:192/binary,Slot:8/integer,Timestamp:64/integer>>
  StationTyp = binary:bin_to_list(Packet,0,1),
  Nutzdaten= binary:bin_to_list(Packet,1,24),
  Slot = binary:decode_unsigned(binary:part(Packet,25,1)),
  Timestamp = binary:decode_unsigned(binary:part(Packet,26,8)),
  {StationTyp,Nutzdaten,Slot,Timestamp}.

% Senden(Kollision) - 1.5 | Senden - 4.1.1: erzeugt ein leeres Message-Objekt mit den angegebene Daten
newMessage(StationTyp, Nutzdaten) ->
  {null, StationTyp, Nutzdaten, null, null}.

%% Getter
% Empfang - 1.2.1: gibt den Frame, in der die Message gesendet werden soll bzw gesandt wurde an
getFrame(Message) ->
  {TargetFrame, _StationTyp, _Nutzdaten, _Slot, _Timestamp} = Message,
  TargetFrame.

% Empfang - alt: gibt die Station an, von der die Message gesendet wurde/wird
getStation(Message) ->
  {_TargetFrame, _StationTyp, Nutzdaten, _Slot, _Timestamp} = Message,
  %Nutzdaten 1-10 = Name der Station
  %{Stationsname, _} = lists:split(10, Nutzdaten), %Alternative Variante
  Stationsname = lists:sublist(Nutzdaten, 10),
  Stationsname.

getData(Message) ->
  {_TargetFrame, _StationTyp, Nutzdaten, _Slot, _Timestamp} = Message,
  Nutzdaten.

getStationTyp(Message) ->
  {_TargetFrame, StationTyp, _Nutzdaten, _Slot, _Timestamp} = Message,
  StationTyp.

getTime(Message) ->
  {_TargetFrame, _StationTyp, _Nutzdaten, _Slot, Timestamp} = Message,
  Timestamp.

getReservedSlot(Message) ->
  {_TargetFrame, _StationTyp, _Nutzdaten, Slot, _Timestamp} = Message,
  Slot.

%% Setter
% Senden - 1.3: setzen des Slots, in dem die Station dieser Nachricht im naechsten Frame senden will
setNextSlot(Message, Slot) ->
  {TargetFrame, StationTyp, Nutzdaten, _, Timestamp} = Message,
  {TargetFrame, StationTyp, Nutzdaten, Slot, Timestamp}.

% Senden - 2: setzt die Sendezeit der Nachricht
setTime(Message, Time) ->
  {TargetFrame, StationTyp, Nutzdaten, Slot, _} = Message,
  {TargetFrame, StationTyp, Nutzdaten, Slot, Time}.

setFrame(Message, Frame) ->
  {_, StationTyp, Nutzdaten, Slot, Timestamp} = Message,
  {Frame, StationTyp, Nutzdaten, Slot, Timestamp}.

setData(Message, Data) ->
  {TargetFrame, StationTyp, _, Slot, Timestamp} = Message,
  {TargetFrame, StationTyp, Data, Slot, Timestamp}.

messageToBinary(Message) ->
  {_TargetFrame, StationTyp, Nutzdaten, Slot, Timestamp} = Message,
  %logging(?LOGFILE, "Wandle Message in binray um, mit Daten: " ++ to_String(StationTyp) ++ ", " ++ to_String(Nutzdaten) ++ ", " ++ to_String(Slot) ++ ", " ++ to_String(Timestamp) ++ "\n"),
  concatBinary(createBinaryS(StationTyp), createBinaryD(Nutzdaten), createBinaryNS(Slot), createBinaryT(Timestamp)).