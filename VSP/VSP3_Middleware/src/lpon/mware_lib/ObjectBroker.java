package lpon.mware_lib;

public class ObjectBroker {

	/**
	 * 
	 * @param serviceHost
	 * @param listenPort
	 * @param debug
	 */
	public static ObjectBroker init(String serviceHost, int listenPort, boolean debug) {
		// TODO - implement ObjectBroker.init		
		throw new UnsupportedOperationException();
		
		
	}

	public NameService getNameService() {
		// TODO - implement ObjectBroker.getNameService
		throw new UnsupportedOperationException();
	}

	public void shutDown() {
		// TODO - implement ObjectBroker.shutDown
		throw new UnsupportedOperationException();
	}
	
	
	private static Thread startSocket(int port) throws IOException {
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
				Connection connection = new Connection(socket, lookUpTable);
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
	
}