package lpon.mware_lib;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
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
		Object result = null;
		
		Class<?>[] paramTypes = new Class[msg.params.length];
		for (int i = 0; i < msg.params.length; i++) {
			paramTypes[i] = msg.params[i].getClass();
		}
		
		Object obj = ReferenceModule.getObject(msg.name);
		
		try {
			Method m = obj.getClass().getDeclaredMethod(msg.methodname, paramTypes);
			result = m.invoke(obj, msg.params);
		} catch (NoSuchMethodException | NullPointerException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@Override
	public void run() {
		super.run();

		try {
			ObjectInputStream reader = new ObjectInputStream(listeningSocket.getInputStream());
			ObjectOutputStream writer = new ObjectOutputStream(listeningSocket.getOutputStream());
			
			MessageCall msg = (MessageCall)reader.readObject();
			MessageReply reply = null;
			try {
				Object result = callMethod(msg);
				reply = new MessageReply(false, result);
			} catch (Exception e) {
				reply = new MessageReply(true, e);
			}
				
			writer.writeObject(reply);
			
			reader.close();
			writer.close();
			listeningSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}
