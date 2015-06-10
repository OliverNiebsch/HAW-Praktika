package mware_lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class CommunicationModule {
	private static int nsPort;
	private static String nsHost;
	private static final String CRLF = "\n";

	public static void init(String nsHost, int nsPort) {
		CommunicationModule.nsPort = nsPort;
		CommunicationModule.nsHost = nsHost;
	}

	/**
	 * 
	 * @param objectID
	 * @param MethodName
	 */
	// Sequenz-Diagramm 3 - Ref 2
	public MessageReply callMethod(MessageCall messageCall, String host,
			int port) {
		Socket con = null;
		try {
			con = new Socket(host, port);
			ObjectOutputStream writer = new ObjectOutputStream(
					con.getOutputStream());
			ObjectInputStream reader = new ObjectInputStream(
					con.getInputStream());
			
			// Sequenz-Diagramm 3 - Ref 2.1
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
				throw new RuntimeException("Ungueltiges Reply Object erhalten");
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
		Socket con = null;

		try {
			con = new Socket(nsHost, nsPort);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			OutputStreamWriter writer = new OutputStreamWriter(
					con.getOutputStream());

			String rebindMessage = "rebind" + "," + host + "," + port + ","
					+ name;
			writeLine(rebindMessage, writer);// SWITCH COMMANDS

			String replyMessage = reader.readLine();
			if (!(null == replyMessage)) {// Socket closed
				ObjectBroker.logger.print(replyMessage);

				if (!replyMessage.contains(",ok")) {
					ObjectBroker.logger
							.print("UNGUELTIGES OBJECT - Konnte nicht zum Namenservice hinzugefuegt werden!!!!!!! FAILED");
				} else {
					ReferenceModule.addObject(name, servant);
				}
			}
			reader.close();
			writer.close();
			con.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeLine(String line, OutputStreamWriter writer) {
		try {
			ObjectBroker.logger.print(line);
			writer.write(line + CRLF);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param id
	 */
	public Object resolveID(String id) {
		Object returnvalue = null;
		Socket con = null;

		try {
			con = new Socket(nsHost, nsPort);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			OutputStreamWriter writer = new OutputStreamWriter(
					con.getOutputStream());

			String resolveMessage = "resolve" + "," + id;
			writeLine(resolveMessage, writer);// SWITCH COMMANDS

			String replyMessage = reader.readLine();
			ObjectBroker.logger.print(replyMessage);

			returnvalue = ResolveMessageToAry(replyMessage);
			reader.close();
			writer.close();
			con.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		if (null == returnvalue) {
			ObjectBroker.logger
					.print("ObjectID konnte nicht korrekt aufgeloest werden: ");
			throw new RuntimeException("Ungueltige Object ID uebergeben");
		}

		return returnvalue;
	}

	private String[] ResolveMessageToAry(String Message) {
		String[] ary = Message.split(",");
		if (ary.length != 4) {
			return null;
		} else {
			String[] returnary = new String[3];
			returnary[0] = ary[1];
			returnary[1] = ary[2];
			returnary[2] = ary[3];
			return returnary;
		}
	}

}