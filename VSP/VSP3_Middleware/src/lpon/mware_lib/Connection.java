package lpon.mware_lib;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * Behandelt Methodenaufrufe von aussen
 */
public class Connection extends Thread {
	
	private Socket listeningSocket = null;

	public Connection(Socket listeningSocket) {
		super();
		this.listeningSocket = listeningSocket;
	}
	
	private Object callMethod(MessageCall msg) throws Exception {
		Object result = null;
		
		// Sequenz-Diagramm 3 - Ref 2.1.1
		Object obj = ReferenceModule.getObject(msg.name);
		
		Method m = null;
		for (Method method : obj.getClass().getMethods()) {
			if (method.getName().equals(msg.methodname)) {
				m = method;
			}
		}
		
		try {
			// Sequenz-Diagramm 3 - Ref 2.1.2
			result = m.invoke(obj, msg.params);
		} catch (InvocationTargetException e) {
			throw (Exception)e.getCause();
		}
		
		return result;
	}
	
	@Override
	public void run() {
		super.run();

		try {
			ObjectInputStream reader = new ObjectInputStream(listeningSocket.getInputStream());
			ObjectOutputStream writer = new ObjectOutputStream(listeningSocket.getOutputStream());
			
			// Sequenz-Diagramm 3 - Ref 2.1 Object lesen
			MessageCall msg = (MessageCall)reader.readObject();
			MessageReply reply = null;
			try {
				Object result = callMethod(msg);
				
				// Sequenz-Diagramm 3 - Ref 2.1.3 MessageReply-Objekt erstellen
				reply = new MessageReply(false, result);
			} catch (Exception e) {
				reply = new MessageReply(true, e);
			}
				
			// Sequenz-Diagramm 3 - Ref 2.1.3 MessageReply-Objekt versenden
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
