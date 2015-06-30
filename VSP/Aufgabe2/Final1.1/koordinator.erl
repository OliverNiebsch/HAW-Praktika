%%%-------------------------------------------------------------------
%%% @author Oliver
%%% @copyright (C) 2015, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 12. Mai 2015 09:52
%%%-------------------------------------------------------------------
-module(koordinator).
-import(werkzeug, [get_config_value/2,logging/2,logstop/0,openSe/2,openSeA/2,openRec/3,openRecA/3,createBinaryS/1,createBinaryD/1,createBinaryT/1,createBinaryNS/1,concatBinary/4,message_to_string/1,shuffle/1,timeMilliSecond/0,reset_timer/3,compareNow/2,getUTC/0,compareUTC/2,now2UTC/1,type_is/1,to_String/1,bestimme_mis/2,testeMI/2]).

%% API
-export([spawnStart/0, start/0]).

%% Code
spawnStart() ->
  spawn(koordinator, start, []).

%% START Initialisierungsphase
start() ->
  % 1: Initialisieren
  {KoordName, NameserviceNode, _NameserviceName, Anzggt, Arbeitszeit, Termizeit, TermiQuote, Korrigieren} = readConfig(),
  LogFile = "Koordinator.log",

  % Nameservice holen
  net_adm:ping(NameserviceNode),
  timer:sleep(400),
  Nameservice = global:whereis_name(nameservice),

  % 2: beim Nameservice registrieren
  register(KoordName, self()),
  Nameservice ! {self(), {bind, KoordName, node()}},
  receive ok -> logging(LogFile, "bind Koordinator done.\n")
  end,

  receiveBlocks(Nameservice, LogFile, Arbeitszeit, Termizeit, TermiQuote, Anzggt, Korrigieren),

  % Koordinator wurde mit einem kill Kommando beendet
  Nameservice ! {self(),{unbind,KoordName}},
  receive
    ok -> io:format("..unbind..done.\n")
  end,
  unregister(KoordName).

receiveBlocks(Nameservice, Logfile, Arbeitszeit, Termizeit, TermiQuote, Anzggt, Korrigieren) ->
  InitialResult = initialReceive([], Nameservice, Logfile, Arbeitszeit, Termizeit, TermiQuote, Anzggt, 0),

  case InitialResult of
    {ok, Clients} ->
      ReadyReceive = readyReceive(Clients, Nameservice, Logfile, TermiQuote, -1, Korrigieren),

      if
        ReadyReceive == reset ->
          killAll(Clients, Nameservice, Logfile),
          flush(),
          receiveBlocks(Nameservice, Logfile, Arbeitszeit, Termizeit, TermiQuote, Anzggt, Korrigieren);
        true -> true
      end;
    _ -> true
  end.

% Leert die MessageQueue
flush() ->
  receive
    _ -> true
    after 0 -> true
  end.

readConfig() ->
  {ok, ConfigListe} = file:consult("koordinator.cfg"),
  {ok, Arbeitszeit} = get_config_value(arbeitszeit, ConfigListe),
  {ok, Termizeit} = get_config_value(termzeit, ConfigListe),
  {ok, Anzggt} = get_config_value(ggtprozessnummer, ConfigListe),
  {ok, NameserviceNode} = get_config_value(nameservicenode, ConfigListe),
  {ok, NameserviceName} = get_config_value(nameservicename, ConfigListe),
  {ok, KoordName} = get_config_value(koordinatorname, ConfigListe),
  {ok, TermiQuote} = get_config_value(quote, ConfigListe),
  {ok, Korrigieren} = get_config_value(korrigieren, ConfigListe),
  {KoordName, NameserviceNode, NameserviceName, Anzggt, Arbeitszeit, Termizeit, TermiQuote, Korrigieren == 1}.

killAll([], _, Logfile) ->
  logging(Logfile, "Killed all known ggT-Processes\n"),
  killed;

killAll([GgTProzessName | RestProzesse], Nameservice, Logfile) ->
  Nameservice ! {self(),{lookup,GgTProzessName}},
  receive
    not_found ->
      logging(Logfile, to_String(GgTProzessName) ++ " not_found.\n");

    {pin, ClientPID} ->
      logging(Logfile, to_String(GgTProzessName) ++ " send kill command.\n"),
      ClientPID ! kill;

    _Any ->
      true
  end,

  killAll(RestProzesse, Nameservice, Logfile).

initialReceive(Clients, Nameservice, Logfile, ArbeitsZeit, TermZeit, TermiQuote, AnzGGT, StarterCount) ->
  receive
    {StarterPID, getsteeringval} -> % 3: Starter fragt nach Initialisierungswerten
      StarterPID ! {steeringval,ArbeitsZeit,TermZeit,(StarterCount + 1) * AnzGGT * TermiQuote div 100,AnzGGT},
      initialReceive(Clients, Nameservice, Logfile, ArbeitsZeit, TermZeit, TermiQuote, AnzGGT, StarterCount + 1);

    {hello, Clientname} ->  % 4.3: Client meldet sich das erste Mal beim Koordinator
      logging(Logfile, to_String(Clientname) ++ " says hello.\n"),
      initialReceive([Clientname | Clients], Nameservice, Logfile, ArbeitsZeit, TermZeit, TermiQuote, AnzGGT, StarterCount);

    step -> % manueller Befehl zum Übergang in den "Bereit"-Zustand
      Ring = createRing(Clients),  % 5: Ring erzeugen
      [First | _] = Ring,
      setNeighbours(1, First, Ring, length(Ring), Nameservice, Logfile); % 6: Nachbarn setzen

    kill ->
      killAll(Clients, Nameservice, Logfile);

    Any ->
      logging(Logfile, "received something wrong: " ++ to_String(Any) ++ "\n"),
      initialReceive(Clients, Nameservice, Logfile, ArbeitsZeit, TermZeit, TermiQuote, AnzGGT, StarterCount)
  end.

createRing(Clients) ->
  lists:sort(fun(_A, _B) -> random:uniform() < random:uniform() end, Clients).

setNeighbours(Pos, _Elem, Clients, Size, _Nameservice, _Logfile) when Pos > Size ->
  {ok, Clients};

setNeighbours(Pos, Elem, Clients, Size, Nameservice, Logfile) ->
  Prev = lists:nth(((Pos - 1 + Size - 1) rem Size) + 1, Clients),
  Next = lists:nth(((Pos) rem Size) + 1, Clients),

  % PID vom Elem holen
  Nameservice ! {self(),{lookup,Elem}},
  receive
    not_found ->
      logging(Logfile, to_String(Elem) ++ " not_found.\n"),
      setNeighbours(Pos + 1, Next, Clients, Size, Nameservice, Logfile);

    {pin, ClientPID} ->
      % Nachbarn senden
      logging(Logfile, to_String(Elem) ++ " send neighbours: " ++ to_String(Prev) ++ " and " ++ to_String(Next) ++ "\n"),
      ClientPID ! {setneighbors, Prev, Next},
      setNeighbours(Pos + 1, Next, Clients, Size, Nameservice, Logfile);

    kill ->
      killAll(Clients, Nameservice, Logfile)
  end.

%% END Initialisierungsphase

%% START Berechnungsphase
readyReceive(Clients, Nameservice, Logfile, Quote, KleinsterGgT, Korrigieren) ->
  receive
    {calc, WggT} ->
      % 1: loop Mi senden
      Mis = bestimme_mis(WggT, length(Clients)),
      initializeMi(Clients, Mis, Nameservice, Logfile),

      % 2: loop Für 20% der Clients Y senden
      Clients20Percent = random20percent(Clients),
      YValues = bestimme_mis(WggT, length((Clients20Percent))),
      initializeY(Clients20Percent, YValues, Nameservice, Logfile),

      readyReceive(Clients, Nameservice, Logfile, Quote, -1, Korrigieren);

    {briefmi, {GGTProName, CMi, CZeit}} ->
      logging(Logfile, to_String(GGTProName) ++ " sent new Mi: " ++ to_String(CMi) ++ " at " ++ to_String(CZeit) ++ "\n"),
      readyReceive(Clients, Nameservice, Logfile, Quote, KleinsterGgT, Korrigieren);

    {_PID, briefterm, {GGTProName, CMi, CZeit}} when (KleinsterGgT < 0) or (CMi =< KleinsterGgT) ->
      logging(Logfile, to_String(GGTProName) ++ " sent term Mi: " ++ to_String(CMi) ++ " at " ++ to_String(CZeit) ++ ". It's lowest ggT yet!\n"),
      readyReceive(Clients, Nameservice, Logfile, Quote, CMi, Korrigieren);

    {PID, briefterm, {GGTProName, CMi, CZeit}} ->
      logging(Logfile, to_String(GGTProName) ++ " sent term Mi: " ++ to_String(CMi) ++ " at " ++ to_String(CZeit) ++ ". There is a lower ggT: " ++ to_String(KleinsterGgT) ++ "!\n"),

      if
        Korrigieren == true ->
          logging(Logfile, "Send lower ggT to Process " ++ to_String(GGTProName) ++ "\n"),
          PID ! {sendy, KleinsterGgT};
        true -> true
      end,

      readyReceive(Clients, Nameservice, Logfile, Quote, CMi, Korrigieren);

    prompt ->
      TellFun = fun (ClientName, ClientPID, Log) ->
          ClientPID ! {self(), tellmi},
          receive
            {mi,Mi} -> logging(Log, to_String(ClientName) ++ " has current Mi: " ++ to_String(Mi) ++ "\n")
          after 5000 -> logging(Log, to_String(ClientName) ++ " doesn't answer to tellmi!\n")
          end
        end,

      forAllClients(Clients, Nameservice, Logfile, TellFun),
      readyReceive(Clients, Nameservice, Logfile, Quote, KleinsterGgT, Korrigieren);

    nudge ->
      PingFun = fun (ClientName, ClientPID, Log) ->
          ClientPID ! {self(), pingGGT},
          receive
            {pongGGT, ProcessName} -> logging(Log, to_String(ProcessName) ++ "is alive!\n")
          after 5000 -> logging(Log, to_String(ClientName) ++ "is dead!\n")
          end
        end,

      forAllClients(Clients, Nameservice, Logfile, PingFun),
      readyReceive(Clients, Nameservice, Logfile, Quote, KleinsterGgT, Korrigieren);

    toogle ->
      logging(Logfile, "received toggle command, set flag to: " ++ to_String(not(Korrigieren))),
      readyReceive(Clients, Nameservice, Logfile, Quote, KleinsterGgT, not(Korrigieren));

    reset ->
      reset;

    kill ->
      killAll(Clients, Nameservice, Logfile);

    Any ->
      logging(Logfile, "received something wrong: " ++ to_String(Any) ++ "\n"),
      readyReceive(Clients, Nameservice, Logfile, Quote, KleinsterGgT, Korrigieren)
  end.

initializeMi([], _Mis, _Nameservice, _Logfile) ->
  true;

initializeMi([Client | RestClients], [Mi | RestMis], Nameservice, Logfile) ->
  Nameservice ! {self(),{lookup,Client}},
  receive
    not_found ->
      logging(Logfile, to_String(Client) ++ " not_found.\n"),
      initializeMi(RestClients, RestMis, Nameservice, Logfile);

    {pin, ClientPID} ->
      logging(Logfile, to_String(Client) ++ " send initial Mi: " ++ to_String(Mi) ++ "\n"),
      ClientPID ! {setpm, Mi},
      initializeMi(RestClients, RestMis, Nameservice, Logfile);

    kill ->
      self() ! kill
  end.

random20percent(List) ->
  L = (length(List) * 20) div 100,
  if
    L < 2 -> Size = 2;
    true -> Size = L
  end,

  random20percent(List, [], Size).

random20percent(_Clients, NeueListe, Size) when length(NeueListe)==Size ->
  % NeueListe ist groß genug
  NeueListe;

random20percent(Clients, NeueListe, Size) when Size-length(NeueListe)==length(Clients) ->
  % es fehlen noch so viele Elemente, wie noch in der Clientliste vohanden sind
  % also wird die restliche Liste einfach übernommen
  NeueListe ++ Clients;

random20percent([Kopf | Rest], NeueListe, Size) ->
  % Normalfall, zu 20% den Kopf zur neuen Liste hinzufügen
  Rnd = random:uniform(),
  if
    Rnd > 0.8 ->
      NeueListe2 = NeueListe ++ [Kopf];
    true ->
      NeueListe2 = NeueListe
  end,
  random20percent(Rest, NeueListe2, Size).

initializeY([], _YValues, _Nameservice, _Logfile) ->
  true;

initializeY([Client | RestClients], [Y | RestY], Nameservice, Logfile) ->
  Nameservice ! {self(),{lookup,Client}},
  receive
    not_found ->
      logging(Logfile, to_String(Client) ++ " not_found.\n"),
      initializeY(RestClients, RestY, Nameservice, Logfile);

    {pin, ClientPID} ->
      logging(Logfile, to_String(Client) ++ " send initial Y: " ++ to_String(Y) ++ "\n"),
      ClientPID ! {sendy, Y},
      initializeY(RestClients, RestY, Nameservice, Logfile);

    kill ->
      self() ! kill
  end.

forAllClients([], _Nameservie, _Logfile, _F) -> true;

forAllClients([Client | RestClients], Nameservice, Logfile, F) ->
  Nameservice ! {self(),{lookup,Client}},
  receive
    not_found ->
      logging(Logfile, to_String(Client) ++ " not_found.\n"),
      forAllClients(RestClients, Nameservice, Logfile, F);

    {pin, ClientPID} ->
      F(Client, ClientPID, Logfile),
      forAllClients(RestClients, Nameservice, Logfile, F);

    kill ->
      self() ! kill
  end.

%% END Berechnungsphase