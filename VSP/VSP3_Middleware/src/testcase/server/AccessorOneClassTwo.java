package testcase.server;

import accessor_one.ClassTwoImplBase;
import accessor_one.SomeException110;
import accessor_one.SomeException112;

public class AccessorOneClassTwo extends ClassTwoImplBase {
	private Double lastValue = null;

	@Override
	public int methodOne(double param1) throws SomeException110 {
		if (param1 < 0)
			throw new SomeException110("Double ist negativ: " + Double.toString(param1));
		
		lastValue = param1;
		
		return (int)param1;
	}

	@Override
	public double methodTwo() throws SomeException112 {
		if (lastValue == null)
			throw new SomeException112("Kein Double Wert vorhanden");
		
		double ret = lastValue;
		lastValue = null;
		
		return ret;
	}

}
