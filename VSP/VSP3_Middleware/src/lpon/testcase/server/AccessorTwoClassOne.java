package lpon.testcase.server;

import lpon.accessor_two.ClassOneImplBase;
import lpon.accessor_two.SomeException112;
import lpon.accessor_two.SomeException304;

public class AccessorTwoClassOne extends ClassOneImplBase {
	@Override
	public double methodOne(String param1, double param2)
			throws SomeException112 {
		
		double d = 0.0;
		try {
			d = Double.parseDouble(param1);
		} catch(NumberFormatException e) {
			throw new SomeException112("String " + param1 + " ist kein Double Wert");
		}
		
		return d + param2;
	}

	@Override
	public double methodTwo(String param1, double param2)
			throws SomeException112, SomeException304 {
		double d = 0.0;
		try {
			d = Double.parseDouble(param1);
		} catch(NumberFormatException e) {
			throw new SomeException112("String " + param1 + " ist kein Double Wert");
		}
		
		if (param2 < 0) {
			throw new SomeException304("Double Wert ist negativ: " + Double.toString(param2));
		}
		
		return d / param2;
	}

}
