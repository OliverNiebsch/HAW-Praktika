-module(datensenke).

%% API
-export([printAllMessages/1]).

%% Schnittstellen
% Empfang - 4: schreibt alle Nachrichten raus
printAllMessages([]) -> true;

printAllMessages([_Msg | _MsgList]) ->
  %werkzeug:logging("Datensenke.log", message:getData(Msg)),
  %printAllMessages(MsgList),
  true.