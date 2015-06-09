package lpon.name_service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Connection extends Thread {
	private Socket listeningSocket;
//	private InputStreamReader reader = null;  //new InputStreamReader(socket.getInputStream());
//	private OutputStreamWriter writer = null; //new OutputStreamWriter(socket.getOutputStream());
//	private boolean closeConnection = false;
	
	private static final String CRLF = "\n";
	
	public Connection(Socket listeningSocket) {
		super();
		this.listeningSocket = listeningSocket;
	}
	
	/*
	
	*/
	
	private void writeLine(String line, OutputStreamWriter writer) {
		try {
			System.out.println(line);
			writer.write(line + CRLF);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(listeningSocket.getInputStream()));
			OutputStreamWriter writer = new OutputStreamWriter(listeningSocket.getOutputStream());
//			while(!closeConnection){...}
			
			String inputMessage = reader.readLine();
			writeLine(handleInputMessage(inputMessage), writer);//SWITCH COMMANDS
			
			reader.close();
			writer.close();
			listeningSocket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String handleInputMessage(String inputMessage) {
		
		String commandType;
		String value;
		
		if(!inputMessage.contains(",")){
			commandType = "BAD_COMMAND";
			value = "inputMessage";
		}
		
		else{
			int firstSpace = inputMessage.indexOf(",");
			
			commandType = inputMessage.substring(0, firstSpace-1);
			value = inputMessage.substring(firstSpace+1);
		}
		
		//String firstCommand = inputMessage.indexOf(" ");
		
		String result = "";

		switch (commandType) {
		case "BAD_COMMAND":
			result = "UnknownCommand received: " + inputMessage;
			break;
		case "rebind":
			//TODO rebind myHost, myPort, name
			String[] ary = value.split(",");
			
			if(ary.length != 3){
				String myHost = ary[0];
				int myPort = Integer.parseInt(ary[1]);
				String name = ary[2];
				LookUpTable.rebind(name, myHost, myPort);
				result = "rebind_reply ok"; 	
			}
			else{
				result = "rebind_reply failed: RECEIVED:" + inputMessage;
				
			}
			break;
		case "resolve":
			ResolveObject resObj = LookUpTable.resolve(value);
			String myHost = resObj.getHostname();
			String myPort = resObj.getPort() + "";
			String name = resObj.getId();
			result = "resolve_reply " + name + "," + myHost + "," + myPort;
			break;
			
		default:
			result = "COULD NOT HANDLE:: " + inputMessage;
			//throw new RuntimeException(); // - > really bad status
			// break;
		}

		return result;
	}
	
	public void closeConnection() {
		// TODO
	}

	/*
	private String readLine() {
		if (reader == null)
			return null;

		StringBuilder line = new StringBuilder();
		char c = 'a';
		boolean cr = false;

		try {
			while (true) {
				c = (char) reader.read();
				line.append(c);

				if (cr && c == '\n')
					break;

				if (c == '\r') {
					cr = true;
				} else {
					cr = false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.print(line.toString());

		return line.toString();
	}
	*/
}
