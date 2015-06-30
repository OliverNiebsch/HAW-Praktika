-module(message).

%% API
-export([binaryToMessageObj/1, newMessage/1, getFrame/1, getStation/1, setNextSlot/2, setTime/2]).

%% Schnittstellen

%% Initialisierung
% Empfang - 1.1: erzeugt ein Message-Objekt aus binären Daten
binaryToMessageObj(binaryMessage) ->
  true.

% Senden(Kollision) - 1.5 | Senden - 4.1.1: erzeugt ein leeres Message-Objekt mit den angegebene Daten
newMessage(data) ->
  true.


%% Getter
% Empfang - 1.2.1: gibt den Frame, in der die Message gesendet werden soll bzw gesandt wurde an
getFrame(message) ->
  true.

% Empfang - alt: gibt die Station an, von der die Message gesendet wurde/wird
getStation(message) ->
  true.


%% Setter
% Senden - 1.3: setzen des Slots, in dem die Station dieser Nachricht im nächsten Frame senden will
setNextSlot(message, slot) ->
  true.

% Senden - 2: setzt die Sendezeit der Nachricht
setTime(message, time) ->
  true.


%% interne Hilfsmethoden