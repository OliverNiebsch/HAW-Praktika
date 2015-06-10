package lpon.mware_lib;

import java.util.concurrent.ConcurrentHashMap;

public abstract class ReferenceModule {
	private static ConcurrentHashMap<String, Object> objects = new ConcurrentHashMap<String, Object>();
	
	protected static void addObject(String id, Object obj) {
		objects.put(id, obj);
	}
	
	protected static Object getObject(String id) {
		return objects.get(id);
	}
}
