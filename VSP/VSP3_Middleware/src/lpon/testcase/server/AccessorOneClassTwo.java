package lpon.testcase.server;

import lpon.accessor_one.ClassTwoImplBase;
import lpon.accessor_one.SomeException110;
import lpon.accessor_one.SomeException112;

public class AccessorOneClassTwo extends ClassTwoImplBase {
	private Double lastValue = null;

	@Override
	public int methodOne(double param1) throws SomeException110 {
		if (param1 < 0)
			throw new SomeException110("Double ist negativ: " + Double.toString(param1));
		
		return (int)param1;
	}

	@Override
	public double methodTwo() throws SomeException112 {
		if (lastValue == null)
			throw new SomeException112("Kein Double Wert vorhanden");
		
		return lastValue;
	}

}
