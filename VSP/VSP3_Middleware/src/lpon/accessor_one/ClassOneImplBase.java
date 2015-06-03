package lpon.accessor_one;

import lpon.mware_lib.accessor.OneClassOneImpl;

public abstract class ClassOneImplBase {
	public abstract String methodOne(String param1, int param2)
			throws SomeException112;

	public static ClassOneImplBase narrowCast(Object rawObjectRef) {
		return new OneClassOneImpl(rawObjectRef);
	}
}