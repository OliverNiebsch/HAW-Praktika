package lpon.mware_lib;

import java.util.HashMap;

public class NameService {
	/**
	 * 
	 * @param servant
	 * @param name
	 */
	public void rebind(Object servant, String name) {
		new CommunicationModule().rebindObject(servant, name);
	}

	/**
	 * 
	 * @param name
	 */
	public Object resolve(String name) {
		return new CommunicationModule().resolveID(name);
	}

}