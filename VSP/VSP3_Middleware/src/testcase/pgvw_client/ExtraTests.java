package testcase.pgvw_client;

import static org.junit.Assert.*;
import static test.client.werkzeug.printResult;
import mware_lib.NameService;
import mware_lib.ObjectBroker;
import mware_lib.util.PropUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import accessor_two.ClassOneImplBase;

public class ExtraTests {
	
	private NameService ns;
	private ObjectBroker ob;

	@Before
	public void setUp() {
		ob = ObjectBroker.init(PropUtil.getProp("nameserverHost"), PropUtil.getIntProp("nameserverPort"), false);
		ns = ob.getNameService();

	}

	@Test 
	public void rebindNull() {
		RuntimeException e = null;
		try{
			ns.rebind(null, "null");
		}catch(RuntimeException exception){
			e = exception;
		}		
		assertNotNull(e);
	}
	
	@Test 
	public void resolveUnknown() {
		RuntimeException e = null;
		try{
			ns.resolve("Unknown");
		}catch(RuntimeException exception){
			e = exception;
		}		
		assertNotNull(e);
	}
	
	
	@After
	public void after() {
		ob.shutDown();
	}

}
