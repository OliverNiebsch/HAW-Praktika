-module(datenquelle).

%% API
-export([getNextData/0]).

%% Schnittstellen

%% Getter
% Senden(Kollision) - 1.3 | Senden - 4: liefert neue Daten
getNextData() ->
  "team 08-01              ". %DEBUG
  %io:get_chars('', 24).