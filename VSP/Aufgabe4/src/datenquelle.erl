-module(datenquelle).

%% API
-export([startDatenquelle/0, getNextData/1, loop/1, reader/1]).

%% Schnittstellen
startDatenquelle() ->
  InitData = io:get_chars('', 24),
  LoopPID = spawn(datenquelle, loop, [InitData]),
  spawn(datenquelle, reader, [LoopPID]),  % TODO evtl ID speichern zum killen
  LoopPID.

%% Prozesse
loop(Data) ->
  receive
    {put, NewData} -> loop(NewData);
    {get, ReplyPID} ->
      ReplyPID ! {data, Data},
      loop(Data);
    _ ->
      throw("Datenquelle received wrong Message")
  end.

reader(LoopPID) ->
  NewData = io:get_chars('', 24),
  LoopPID ! {put, NewData},
  reader(LoopPID).

%% Getter
% Senden(Kollision) - 1.3 | Senden - 4: liefert neue Daten
getNextData(LoopPID) ->
  LoopPID ! {get, self()},
  receive
    {data, Data} -> Data
  after
    10 ->
      throw("Keine Daten erhalten"),
      null
  end.