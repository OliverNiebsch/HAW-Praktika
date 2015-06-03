package lpon.name_service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Connection extends Thread {
	private Socket listeningSocket;

	public Connection(Socket listeningSocket) {
		super();
		this.listeningSocket = listeningSocket;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(listeningSocket.getInputStream()));
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(listeningSocket.getOutputStream()));
			
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
