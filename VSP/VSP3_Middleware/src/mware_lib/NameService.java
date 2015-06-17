package mware_lib;


public class NameService {
	private final String host;
	private final int port; 
	
	public NameService(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	/**
	 * 
	 * @param servant
	 * @param name
	 */
	public void rebind(Object servant, String name) {
		new CommunicationModule().rebindObject(servant, name, host, port);
	}

	/**
	 * 
	 * @param name
	 */
	public Object resolve(String name) {
		return new CommunicationModule().resolveID(name);
	}

}