package lpon.mware_lib;

import java.util.concurrent.ConcurrentHashMap;

public abstract class ReferenceModule {
	private static ConcurrentHashMap<String, Object> objects = new ConcurrentHashMap<String, Object>();
	
	protected static void addObject(String id, Object obj) { //REF 1.1 @ rebind
		objects.put(id, obj); 
	}
	
	protected static Object getObject(String id) { //REF 2.1.1 @ rmi
		return objects.get(id); //REF 2.1.2
	}
}
