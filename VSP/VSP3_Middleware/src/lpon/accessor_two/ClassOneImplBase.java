package lpon.accessor_two;

import lpon.mware_lib.AccessorClass;


public abstract class ClassOneImplBase extends AccessorClass {
	public ClassOneImplBase() {
		super(null);
	}
	
	private ClassOneImplBase(Object rawObjRef) {
		super(rawObjRef);
	}
	
	public abstract double methodOne(String param1, double param2)
			throws SomeException112;

	public abstract double methodTwo(String param1, double param2)
			throws SomeException112, SomeException304;

	public static ClassOneImplBase narrowCast(Object rawObjectRef) {
		return new ClassOneImplBase(rawObjectRef) {
			
			@Override
			public double methodTwo(String param1, double param2)
					throws SomeException112, SomeException304 {
				try {
					return (Double)super.remoteCall("methodTwo", new Object[] {param1, param2});
				} catch (SomeException112 e) {
					throw e;
				} catch (Exception e) {
					throw (SomeException304)e;
				}
			}
			
			@Override
			public double methodOne(String param1, double param2)
					throws SomeException112 {
				try {
					return (Double)super.remoteCall("methodOne", new Object[] {param1, param2});
				} catch (Exception e) {
					throw (SomeException112)e;
				}
			}
		};
	}
}