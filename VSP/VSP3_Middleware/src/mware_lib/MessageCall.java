package mware_lib;

import java.io.Serializable;

public class MessageCall implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4782527426008304926L;
	public String name;
	public String methodname;
	public Object[] params;

	public MessageCall(String name, String methodname, Object... params) {
		super();
		this.name = name;
		this.methodname = methodname;
		this.params = params;
	}

}
