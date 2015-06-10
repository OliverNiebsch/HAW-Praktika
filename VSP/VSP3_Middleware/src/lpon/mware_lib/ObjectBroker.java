package lpon.mware_lib;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import lpon.Logger;

public class ObjectBroker {
	private static String host;
	private static int port = 0;
	
	public static Logger logger = new Logger();
	
	private static ObjectBroker instance = null;
	private static Thread listeningThread;
	private static ServerSocket serverSocket;

	private NameService nameService;
	
	/**
	 * @param serviceHost
	 * @param listenPort
	 * @param debug
	 */
	public static ObjectBroker init(String serviceHost, int listenPort, boolean debug) {		
		logger.log = debug;
		if (instance == null) {
			listeningThread = startSocket(port);
			logger.print("ListeningSocket gestartet");
			
			try {
				host = InetAddress.getLocalHost().getHostName();
				logger.print("Host gesetzt auf %s und Port auf %d", host, port);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			instance = new ObjectBroker();
			
			CommunicationModule.init(serviceHost, listenPort);
			logger.print("CommunicationModule initialisiert");
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
		try {
			serverSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		instance = null;
	}
	
	
	private static Thread startSocket(final int port) {
		Thread t = new Thread() {
			
			@Override
			public void run() {
				ArrayList<Connection> connections = new ArrayList<Connection>();
				
				try {
					serverSocket = new ServerSocket(port);
					ObjectBroker.port = serverSocket.getLocalPort();

					while (!isInterrupted()) {
						if (connections.size() < 100) {
							
							// Sequenz-Diagramm 3 - Ref 2.1 TCP Connection annehmen
							Socket socket = serverSocket.accept();
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
					
				} catch (SocketException e) {
					if (!isInterrupted())
						e.printStackTrace();
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