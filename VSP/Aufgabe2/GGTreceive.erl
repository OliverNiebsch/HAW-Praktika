
%halfTimeoutFlag is set to true, if 50% of timeout passed.
%In that case terminateVotes are answered with voteYes
%Otherwise discard msg

% halfTimeoutFlag
% == notimeout >> Noch kein Timeout gekommen
% == halftimeout >> Erstes mal ein Timeout
% == voteRunning >> Es läuft gerade ein Vote von diesem Prozess

receiveFunction(HalfTimeoutFlag, CurrentMi, TimerPID) ->
	receive
		vote when halfTimeoutFlag =/= notimeout -> %Wenn halftimeout ODER voteRunning > Andere votes bestätigen
			%Reply ! voteYes (terminate)
			true;
		voteYes when halfTimeoutFlag =:= voteRunning ->
			% Quote berechnen ob schon genug
			true;			
		{sendy, Y} ->
			%Start algorithm
			TimerPID ! resettimer,
			receiveFunction(notimeout, newMi);
		{sendmi, Mi} ->
			%Set my Mi
			TimerPID ! resettimer %restartTimer
			receiveFunction(notimeout, Mi);			
		kill ->
			%Kill this process
			true;
		halfTimeoutMessage when halfTimeoutFlag =:= notimeout -> %Erstes mal Timeout/2
			%Half timeout passed
			receiveFunction(halftimeout, CurrentMi, TimerPID);
		halfTimeoutMessage when halfTimeoutFlag =:= halftimeout -> %Zweites mal Timeout/2
			%Second timeout/2 received -> Try to vote timeout
			
			receiveFunction(voteRunning, CurrentMi, TimerPID);
		totalTimeOutMessage ->
			%Start vote to terminate
			%No Message received within timeout
			true;
		Any ->
			%Discard unwanted msg
			receiveFunction(halfTimeoutFlag)
	end.

	
setTimer(Pid, TimeMS, TimeoutReplyMsg) ->
	receive
		resettimer ->
			setTimer(Pid, timeMS);
		kill ->
			true
	after
		TimeMS ->
			Pid ! TimeoutReplyMsg
	end,
	
	setTimer(Pid, TimeMS, TimeoutReplyMsg). %Restart timer
	


initTimer(TimeoutInMS) -> %INIT WITH timeout/2
	spawn(timer, setTimer, [self(), TimeoutInMS, halftimeout]).

	
	
%Flush current Queue. 
flush() ->
	receive
		kill -> 
			%Kill this process
			true;			
		Any ->
			flush()
	after
		0 -> %Mit 0 aufrufen > Ruft dadurch erst alle Messages ab und stellt sicher das Queue leer ist
			% Restart listening block
			true
	end.

