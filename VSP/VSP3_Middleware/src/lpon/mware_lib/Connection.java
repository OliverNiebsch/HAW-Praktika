package lpon.mware_lib;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Behandelt Methodenaufrufe von außen
 */
public class Connection extends Thread {
	
	private Socket listeningSocket = null;

	public Connection(Socket listeningSocket) {
		super();
		this.listeningSocket = listeningSocket;
	}
	
	private Object callMethod(MessageCall msg) throws Exception {
		return null;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		try {
			ObjectInputStream reader = new ObjectInputStream(listeningSocket.getInputStream());
			ObjectOutputStream writer = new ObjectOutputStream(listeningSocket.getOutputStream());
			
			MessageCall msg = (MessageCall)reader.readObject();
			MessageReply reply = null;
			try {
				callMethod(msg);
			} catch (Exception e) {
				
			}
				
			writer.writeObject(reply);
			reader.close();
			writer.close();
			listeningSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
