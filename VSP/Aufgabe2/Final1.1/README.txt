########################################################

############################
# README: VSP2 : Gruppe 08 #
############################

1. Anpassen der Konfigurationswerte in den Dateien "ggt.cfg" und "koordinator.cfg"

2. Kompilieren der Module in einer Erlangnode
   Befehl: c(werkzeug), c(koordinator), c(starter), c(ggt).

3. Ausf�hren einer ErlangNode mit gew�nschtem Namen und Cookie f�r jeden Dienst. (Im Ordner der
   beam-Dateien.)
   (Die Ausf�hrreihenfolge ist einzuhalten.)

---------------------------------------------------------------------------------
Z.B. unter Linux:
1. Nameservice:	erl -name nsNode -setcookie vsp -run nameservice start
2. koordinator:	erl -name koordinator -setcookie vsp -run koordinator spawnStart
3. starter: 	erl -name starter -setcookie vsp
---------------------------------------------------------------------------------

//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  Alternativ �ber die mitgelieferten Shell-Scripte:
1. sh compile.sh
2. sh nameservice.sh
3. sh koordinator.sh
4. sh starterggt.sh
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


Mit den Befehlen wurden bereits der Nameservice mit nameservice:start() und der koordinator mit koordinator:spawnStart() initialisiert.

Anschlie�end k�nnen in der Starter-Konsole die ggt-Prozesse gestartet werden:
Beispiel f�r 2 Starter: starter:start(1), starter:start(2).

~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Das System ist nun bereit Berechnungen durchzuf�hren und kann �ber den Koordinator, entsprechend der Interface-Beschreibung, bedient werden.

Beispiel:
chef ! step.
chef ! {calc, 13}.

########################################################