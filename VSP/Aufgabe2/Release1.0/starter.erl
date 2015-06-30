%%%-------------------------------------------------------------------
%%% @author Oliver
%%% @copyright (C) 2015, <COMPANY>
%%% @doc
%%%
%%% @end
%%% Created : 19. Mai 2015 13:06
%%%-------------------------------------------------------------------
-module(starter).
-import(werkzeug, [get_config_value/2, logging/2, to_String/1]).
-import(ggt, [start/9]).

%% API
-export([start/1]).

start(Nr) ->
  % Config Datei auslesen
  {Praktikumsgruppe, Teamnummer, KoordName, NameserviceNode, _NameserviceName} = readConfig(),
  Logfile = "Starter.log",

  % Nameservice holen
  net_adm:ping(NameserviceNode),
  timer:sleep(400),
  Nameservice = global:whereis_name(nameservice),

  % Koordinator holen
  Nameservice ! {self(),{lookup,KoordName}},
  receive
    {pin, Koordinator} ->
      % weitere Informationen vom Koordinator holen
      Koordinator ! {self(),getsteeringval},
      receive
        {steeringval,ArbeitsZeit,TermZeit,Quota,GGTProzessnummer} ->
          createGgTProcesses(GGTProzessnummer, 1, ArbeitsZeit, TermZeit, Quota, Praktikumsgruppe, Teamnummer, Nr, Nameservice, KoordName)
      end;

    not_found ->
      logging(Logfile, to_String(KoordName) ++ " not_found.\n")
  end.

readConfig() ->
  {ok, ConfigListe} = file:consult("ggt.cfg"),
  {ok, PraktikumsGruppe} = get_config_value(praktikumsgruppe, ConfigListe),
  {ok, TeamNummer} = get_config_value(teamnummer, ConfigListe),
  {ok, NameserviceNode} = get_config_value(nameservicenode, ConfigListe),
  {ok, NameserviceName} = get_config_value(nameservicename, ConfigListe),
  {ok, KoordName} = get_config_value(koordinatorname, ConfigListe),
  {PraktikumsGruppe, TeamNummer, KoordName, NameserviceNode, NameserviceName}.

createGgTProcesses(MaxGgTs, MaxGgTs, ArbeitsZeit, Timeout, Quota, Praktikumsgruppe, Teamnummer, StarterNummer, Nameservice, KoordinatorName) ->
  spawn(ggt, start, [ArbeitsZeit, Timeout, Quota, Praktikumsgruppe, Teamnummer, MaxGgTs, StarterNummer, Nameservice, KoordinatorName]);

createGgTProcesses(MaxGgTs, GgTNummer, ArbeitsZeit, Timeout, Quota, Praktikumsgruppe, Teamnummer, StarterNummer, Nameservice, KoordinatorName) ->
  spawn(ggt, start, [ArbeitsZeit, Timeout, Quota, Praktikumsgruppe, Teamnummer, GgTNummer, StarterNummer, Nameservice, KoordinatorName]),
  createGgTProcesses(MaxGgTs, GgTNummer + 1, ArbeitsZeit, Timeout, Quota, Praktikumsgruppe, Teamnummer, StarterNummer, Nameservice, KoordinatorName).