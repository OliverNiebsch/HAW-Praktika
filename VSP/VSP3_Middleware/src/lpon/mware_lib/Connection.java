package lpon.mware_lib;

import java.net.Socket;


public class Connection extends Thread {
	
	private Socket listeningSocket = null;

	public Connection(Socket listeningSocket) {
		super();
		this.listeningSocket = listeningSocket;
	}
	
	
}
