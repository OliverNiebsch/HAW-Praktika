-module(sender).
-import(werkzeug, [get_config_value/2,logging/2,logstop/0,openSe/2,openSeA/2,openRec/3,openRecA/3,createBinaryS/1,createBinaryD/1,createBinaryT/1,createBinaryNS/1,concatBinary/4,message_to_string/1,shuffle/1,timeMilliSecond/0,reset_timer/3,compareNow/2,getUTC/0,compareUTC/2,now2UTC/1,type_is/1,to_String/1,bestimme_mis/2,testeMI/2]).
-export(all).


initSendAdapter() -> 
% openSe(IP,Port) -> Socket % diesen Prozess PidSend (als Nebenläufigenprozess gestartet) bekannt geben mit
	Teamnummer =  8,
	ZielAddr = {225,10,1,2},
	LocalAdress = whatismyip,
	Port = 15000 + Teamnummer,

	Socket = openSe(LocalAdress, Port), 
	gen_udp:controlling_process(Socket, self()), %Anmelden als handler für Socket
	
% senden  mit gen_udp:send(Socket, Addr, Port, <MESSAGE>)
	{Socket, ZielAddr, Port}.
	
multicastSend(SendAdapter, Message) ->
	{Socket, ZielAddr, Port} = SendAdapter,
	gen_udp:send(Socket, ZielAddr, Port, Message)
	

	