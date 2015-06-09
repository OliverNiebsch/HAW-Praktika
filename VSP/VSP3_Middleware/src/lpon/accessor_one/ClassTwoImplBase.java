package lpon.accessor_one;

import lpon.mware_lib.AccessorClass;

public abstract class ClassTwoImplBase extends AccessorClass {
	
	protected ClassTwoImplBase(Object rawObjRef) {
		super(rawObjRef);
	}

	public abstract int methodOne(double param1) throws SomeException110;

	public abstract double methodTwo() throws SomeException112;

	public static ClassTwoImplBase narrowCast(Object rawObjectRef) {
		return new ClassTwoImplBase(rawObjectRef) {
			
			@Override
			public double methodTwo() throws SomeException112 {
				try {
					return (Double)super.remoteCall("methodTwo", new Object[0]);
				} catch (Exception e) {
					throw (SomeException112)e;
				}
			}
			
			@Override
			public int methodOne(double param1) throws SomeException110 {
				try {
					return (Integer)super.remoteCall("methodOne", new Object[]{param1});
				} catch (Exception e) {
					throw (SomeException110)e;
				}
			}
		};
	}
}
