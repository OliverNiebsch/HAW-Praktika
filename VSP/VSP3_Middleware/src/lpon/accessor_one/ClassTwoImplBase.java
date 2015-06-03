package lpon.accessor_one;

import lpon.mware_lib.accessor.OneClassTwoImpl;

public abstract class ClassTwoImplBase {
	public abstract int methodOne(double param1) throws SomeException110;

	public abstract double methodTwo() throws SomeException112;

	public static ClassTwoImplBase narrowCast(Object rawObjectRef) {
		return new OneClassTwoImpl(rawObjectRef);
	}
}
