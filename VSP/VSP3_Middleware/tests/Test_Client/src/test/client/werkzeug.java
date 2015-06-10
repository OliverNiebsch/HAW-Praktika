package test.client;

public class werkzeug {
	public static void printError(Object object,
			String remotetObjName, String methodName, Throwable exaption, Object... params ) {
		System.out.println("--------------------------------------------------------------");
		System.out.println(object.getClass().getSuperclass().getName() + " ('"+ remotetObjName + "')");
		System.out.println(methodName);
		
		int i = 1;
		for(Object param : params){
			if(param!=null){
				System.out.println("param "+i+" = "+ param.getClass().getName() +" : " + param);
			}else{
				System.out.println("param"+i+" = " + param);
			}
			
		
			i++;
		}

		System.out.println(exaption.getClass().getName() + " with message '" + exaption.getMessage() + "'");
		System.out.println("--------------------------------------------------------------");
	}
	
	public static void printResult(Object object, String remotetObjName,String methodName,Object result, Object... params){
		System.out.println("--------------------------------------------------------------");
		System.out.println(object.getClass().getSuperclass().getName() + " ('"+ remotetObjName + "')");
		System.out.println(methodName);
		
		int i = 1;
		for(Object param : params){
			if(param!=null){
				System.out.println("param "+i+" = "+ param.getClass().getName() +" : " + param);
			}else{
				System.out.println("param"+i+" = " + param);
			}
			
		
			i++;
		}
		
		
		System.out.println("Return value = " + result);
		System.out.println("--------------------------------------------------------------");
		
		
	}

}
