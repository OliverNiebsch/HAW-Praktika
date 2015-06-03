package lpon.accessor_two;

import lpon.mware_lib.accessor.TwoClassOneImpl;

public abstract class ClassOneImplBase {
	public abstract double methodOne(String param1, double param2)
			throws SomeException112;

	public abstract double methodTwo(String param1, double param2)
			throws SomeException112, SomeException304;

	public static ClassOneImplBase narrowCast(Object rawObjectRef) {
		return new TwoClassOneImpl(rawObjectRef);
	}
}