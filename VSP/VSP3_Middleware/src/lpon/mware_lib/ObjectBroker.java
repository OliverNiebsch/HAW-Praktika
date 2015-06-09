package lpon.mware_lib;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ObjectBroker {
	private static String host = null;
	private static int port = 13030;
	
	private static ObjectBroker instance = null;
	private static Thread listeningThread;

	private NameService nameService;
	
	/**
	 * 
	 * @param serviceHost
	 * @param listenPort
	 * @param debug
	 */
	public static ObjectBroker init(String serviceHost, int listenPort, boolean debug) {		
		if (instance == null) {
			instance = new ObjectBroker();
			listeningThread = startSocket(port);
			
			CommunicationModule.init(serviceHost, listenPort);
			// TODO: Logger
		} else {
			CommunicationModule.init(serviceHost, listenPort);
		}
		
		return instance;
	}
	
	private ObjectBroker() {
		nameService = new NameService(host, port);
	}

	public NameService getNameService() {
		return nameService;
	}

	public void shutDown() {
		listeningThread.interrupt();
		instance = null;
	}
	
	
	private static Thread startSocket(int port) {
		Thread t = new Thread() {
			
			@Override
			public void run() {
				ArrayList<Connection> connections = new ArrayList<Connection>();
				
				try {
					ServerSocket serverSocket = new ServerSocket(port);
					host = serverSocket.getInetAddress().getHostName();

					while (!isInterrupted()) {
						if (connections.size() < 100) {
							Socket socket = serverSocket.accept(); // ex con
							System.out.println("New Connection established.");
							Connection connection = new Connection(socket);
							connection.start();
							connections.add(connection);
						} else {
							try {
								System.out.println("Too many open connections. Sleeping for 1000ms");
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
	
					}
					
					serverSocket.close();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				for (Connection c : connections) {
					c.interrupt();
				}
			}
		};
		
		t.start();
		
		return t;
	}
	
}