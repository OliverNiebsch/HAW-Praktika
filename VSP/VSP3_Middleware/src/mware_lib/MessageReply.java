package mware_lib;

import java.io.Serializable;

public class MessageReply implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -695108319544890240L;

	public MessageReply(boolean isException, Object result) {
		super();
		this.isException = isException;
		this.result = result;
	}

	public boolean isException;
	public Object result;
}
