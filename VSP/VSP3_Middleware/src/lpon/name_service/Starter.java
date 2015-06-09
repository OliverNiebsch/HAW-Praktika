package lpon.name_service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Starter {
	private static ServerSocket serverSocket;
	private static final int MAX_CLIENT_CON = 100;
	private static final List<Connection> connectedClientThreads = new ArrayList<Connection>();
	private static boolean isShuttingDown = false;
	
//	private InputStreamReader reader = null;/
//	private OutputStreamWriter writer = null;//

	
	public static void main(String[] args) throws IOException {

		int port = 1337;

		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
			System.out.println("Starting Nameserver on Port: " + port);
		}

		startSocket(port);

		System.out.println("Nameserver shutting down");
	}

	private static void startSocket(int port) throws IOException {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		while (!isShuttingDown) {

			cleanUpClientThreads();

			if (connectedClientThreads.size() < MAX_CLIENT_CON) {
				Socket socket = serverSocket.accept(); // ex con
				System.out.println("New Nameservice Connection established.");
				Connection connection = new Connection(socket);
				connection.start();
				connectedClientThreads.add(connection);
			} else {
				try {
					System.out.println("Too many open connections. Sleeping for 1000ms");
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}
		closeClientConnections();
	}

	private static void cleanUpClientThreads() {
		int i = 0;
		while (i < connectedClientThreads.size()) {
			if (!connectedClientThreads.get(i).isAlive()) {
				connectedClientThreads.remove(i);
			} else {
				i++;
			}
		}
	}

	public static void closeClientConnections() throws IOException {
		// Keine neuen Verbindungen annehmen
		serverSocket.close();
		isShuttingDown = true;

		// Close Client Connections
		for (Connection connectionThread : connectedClientThreads) {
			if (null != connectedClientThreads) {
				connectionThread.closeConnection();
			}
		}
	}
}
