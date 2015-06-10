package test.server.accessor_two;

import accessor_two.ClassOneImplBase;
import accessor_two.SomeException112;
import accessor_two.SomeException304;

public class Accessor_Two_ServerClaseOneImpl extends ClassOneImplBase{

	/**
	 * Eine String Zahl + eine Double Zahl
	 */
	@Override
	public double methodOne(String param1, double param2)
			throws SomeException112 {
		
		if(param1==null){
			throw new SomeException112("param1==null");
		}
		
		try{
			double param1Double = Double.parseDouble(param1);			
			return param1Double+param2;
		}catch( Exception e){
			throw new SomeException112("param1 keine Zahl");
		}
	
	}

	/**
	 * Eine String Zahl mal eine Double Zahl
	 * SomeException112 wenn param1 == null
	 * SomeException304 wenn param1 keine zahl
	 */
	@Override
	public double methodTwo(String param1, double param2)
			throws SomeException112, SomeException304 {
		if(param1==null){
			throw new SomeException112("param1==null");
		}
		
		try{
			double param1Double = Double.parseDouble(param1);			
			return param1Double*param2;
		}catch( Exception e){
			throw new SomeException304("param1 keine Zahl");
		}
	}

}
