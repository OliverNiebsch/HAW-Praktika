package lpon.testcase.server;

import lpon.accessor_one.ClassOneImplBase;
import lpon.accessor_one.SomeException112;

public class AccessorOneClassOne extends ClassOneImplBase {
	@Override
	public String methodOne(String param1, int param2) throws SomeException112 {
		if (param2 < 0) {
			throw new SomeException112("Integer ist negativ: " + param2);
		}
		
		return param1 + " Zahl: " + param2;
	}

}
