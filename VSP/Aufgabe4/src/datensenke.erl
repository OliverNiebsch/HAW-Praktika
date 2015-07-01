-module(datensenke).

%% API
-export([printAllMessages/1]).

%% Schnittstellen
% Empfang - 4: schreibt alle Nachrichten raus
printAllMessages([]) -> true;

printAllMessages([Msg | MsgList]) when (is_list(Msg) =:= true) ->
  printAllMessages(Msg),
  printAllMessages(MsgList);

printAllMessages([Msg | MsgList]) ->
  werkzeug:logging("Datensenke.log", message:getData(Msg)),
  printAllMessages(MsgList).