package lpon.mware_lib.accessor;

public abstract class AccessorClass {
//	public static Object NARROW_CAST(String packageName, String className, Object rawRef) {
//		Object narrowObj = null;
//		
//		switch (packageName) {
//		case "accessor_one":
//			switch (className) {
//			case "ClassOneImplBase":
//				
//				break;
//
//			case "ClassTwoBaseImpl":
//				break;
//			}
//			break;
//
//		case "accessor_two":
//			switch (className) {
//			case "ClassOneImplBase":
//				
//				break;
//			}
//			break;
//		}
//		
//		return narrowObj;
//	}
	
	protected AccessorClass(Object rawObjRef) {
		// TODO: implementieren
	}
	
	protected Object remoteCall(String methodName, Object[] params) throws Exception {
		// TODO: implementieren
		return null;
	}
}
