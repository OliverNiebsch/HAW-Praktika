package lpon.accessor_one;

import lpon.mware_lib.AccessorClass;


public abstract class ClassOneImplBase extends AccessorClass {
	public ClassOneImplBase() {
		super(null);
	}
	
	public ClassOneImplBase(Object rawObjRef) {
		super(rawObjRef);
	}
	
	public abstract String methodOne(String param1, int param2)
			throws SomeException112;

	public static ClassOneImplBase narrowCast(Object rawObjectRef) {
		return new ClassOneImplBase(rawObjectRef) {
			
			@Override
			public String methodOne(String param1, int param2) throws SomeException112 {
				try {
					return (String)super.remoteCall("methodOne", new Object[] {param1, param2});
				} catch (SomeException112 e) {
					throw (SomeException112)e;
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return null;
			}
		};
	}
}