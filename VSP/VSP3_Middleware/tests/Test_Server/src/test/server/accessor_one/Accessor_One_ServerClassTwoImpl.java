package test.server.accessor_one;

import accessor_one.ClassTwoImplBase;
import accessor_one.SomeException110;
import accessor_one.SomeException112;

public class Accessor_One_ServerClassTwoImpl extends ClassTwoImplBase {

	private  int count = 0;
	
	@Override
	public int methodOne(double param1) throws SomeException110 {
		
		if(param1<0){
			throw new SomeException110("Zu klein");
		}
		return (int) (param1 * 10); 
	}

	@Override
	public synchronized double methodTwo() throws SomeException112 {
		if (count % 2 == 0) {
			count++;
			throw new SomeException112("Erste Ausführung führt zu Fehler!");
		}
		
		
		return 999.9999;
	}

}
