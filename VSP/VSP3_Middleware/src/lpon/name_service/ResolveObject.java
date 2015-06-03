package lpon.name_service;

public class ResolveObject {
	private String hostname;
	private int port;
	private String id;
	
	public ResolveObject(String hostname, int port, String id) {
		this.hostname = hostname;
		this.port = port;
		this.id = id;
	}

	public String getHostname() {
		return hostname;
	}

	public int getPort() {
		return port;
	}

	public String getId() {
		return id;
	}
}