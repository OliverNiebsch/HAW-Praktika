package lpon.name_service;

import java.util.HashMap;

abstract class LookUpTable {
	private static HashMap<String, ResolveObject> objects = new HashMap<String, ResolveObject>();
	
	public static void rebind(String id, String host, int port) {
		objects.put(id, new ResolveObject(host, port, id));
	}
	
	public static ResolveObject resolve(String id) {
		return objects.get(id);
	}
}
