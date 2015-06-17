package testcase.pgvw_server.accessor_one;

import accessor_one.ClassOneImplBase;
import accessor_one.SomeException112;

public class Accessor_One_ServerClassOneImpl extends ClassOneImplBase{

	@Override
	public String methodOne(String param1, int param2) throws SomeException112 {
		
		
		
		try{
			Thread.sleep(param2);
			int param1Int = Integer.parseInt(param1);			
			return Integer.toString(param1Int+param2);
		}catch( Exception e){
			throw new SomeException112("param1 keine Zahl");
		}
		
		
	}

}
