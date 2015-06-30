-module(receiver).
-import(werkzeug, [get_config_value/2,logging/2,logstop/0,openSe/2,openSeA/2,openRec/3,openRecA/3,createBinaryS/1,createBinaryD/1,createBinaryT/1,createBinaryNS/1,concatBinary/4,message_to_string/1,shuffle/1,timeMilliSecond/0,reset_timer/3,compareNow/2,getUTC/0,compareUTC/2,now2UTC/1,type_is/1,to_String/1,bestimme_mis/2,testeMI/2]).

%% API
-export([start/0]).

%% Schnittstellen

%% MAIN
% startet und initialisiert das ReceiveModul und alle benötigten anderen Module
start() ->
  true.

% Empfang - 1: HauptReceiveBlock für die Anwendung
waitForMessage(Logfile) ->
  receive
    Any ->
      logging(Logfile, "received something wrong: " ++ to_String(Any) ++ "\n"),
      waitForMessage()
  end,
  true.