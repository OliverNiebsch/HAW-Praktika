package lpon.name_service;

import java.util.concurrent.ConcurrentHashMap;

public class LookUpTable {
	private static ConcurrentHashMap<String, ResolveObject> objects = new ConcurrentHashMap<String, ResolveObject>();
	
	public static void rebind(String id, String host, int port) {
		objects.put(id, new ResolveObject(host, port, id));
	}
	
	public static ResolveObject resolve(String id) {
		return objects.get(id);
	}
}
