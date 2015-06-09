package lpon.mware_lib;

public class CommunicationModule {
	private static int nsPort;
	private static String nsHost;
	
	public static void init(String nsHost, int nsPort){
		CommunicationModule.nsPort = nsPort;
		CommunicationModule.nsHost = nsHost;
	}
	
	/**
	 * 
	 * @param objectID
	 * @param MethodName
	 */
	public MessageReply callMethod(MessageCall messageCall, String host, int port) {
		// TODO - implement CommunicationModule.callMethod
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @param servant
	 * @param name
	 */
	public void rebindObject(Object servant, String name) {
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