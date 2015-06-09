package lpon.mware_lib;

public abstract class AccessorClass {
	private String id, host;
	private int port;
	
	protected AccessorClass(Object rawObjRef) {
		if (rawObjRef != null) {
			if (!(rawObjRef instanceof String[])) {
				throw new RuntimeException("Raw data are wrong!");
			}
			
			String[] data = (String[])rawObjRef;
			if (data.length != 3) {
				throw new RuntimeException("Wrong number of Strings in raw data!");
			}
			
			id = data[0];
			host = data[1];
			port = Integer.parseInt(data[2]);
		}
	}
	
	protected Object remoteCall(String methodName, Object[] params) throws Exception {
		CommunicationModule comMod = new CommunicationModule();
		MessageReply msgResult = comMod.callMethod(new MessageCall(id, methodName, params), host, port);
		
//		ObjectBroker.logger.print("Called Method %s on Object %s -> Result of Type %s: %s", methodName, id, result.getClass().toString(), result.toString());
		
		if (msgResult.isException) {
			throw (Exception)msgResult.result;
		} else {
			return msgResult.result;
		}
	}
}
