package lpon.mware_lib;

import java.util.HashMap;

public abstract class ReferenceModule {
	private static HashMap<String, Object> objects = new HashMap<String, Object>();
	
	protected static void addObject(String id, Object obj) {
		objects.put(id, obj);
	}
	
	protected static Object getObject(String id) {
		return objects.get(id);
	}
}
