package lpon.mware_lib;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class CommunicationModule {
	private static int nsPort;
	private static String nsHost;

	public static void init(String nsHost, int nsPort) {
		CommunicationModule.nsPort = nsPort;
		CommunicationModule.nsHost = nsHost;
	}

	/**
	 * 
	 * @param objectID
	 * @param MethodName
	 */
	public MessageReply callMethod(MessageCall messageCall, String host, int port) {
		Socket con = null;
		try {
			con = new Socket(host, port);
			ObjectOutputStream writer = new ObjectOutputStream(
					con.getOutputStream());
			ObjectInputStream reader = new ObjectInputStream(
					con.getInputStream());
			writer.writeObject(messageCall);
			writer.flush();

			Object rawReply = null;
			try {
				rawReply = reader.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}

			if (!(rawReply instanceof MessageReply)) {
				// Communication der Middleware mit sich selbst ist nicht
				// correct.
				throw new RuntimeException("Ungültiges Reply Object erhalten");
			}
			MessageReply messageReply = (MessageReply) rawReply;
			con.close();
			return messageReply;
		} catch (UnknownHostException e) {
			e.printStackTrace();// TODO Auto-generated catch block
		} catch (IOException e) {
			e.printStackTrace();// TODO Auto-generated catch block
		}
		return null;
	}

	/**
	 * 
	 * @param servant
	 * @param name
	 */
	public void rebindObject(Object servant, String name, String host, int port) {
		// TODO - implement CommunicationModule.rebindObject
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param id
	 */
	public Object resolveID(String id) {
		// TODO - implement CommunicationModule.resolveID
		throw new UnsupportedOperationException();
	}

}