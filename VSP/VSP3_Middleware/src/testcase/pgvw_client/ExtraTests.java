package testcase.pgvw_client;

import static org.junit.Assert.assertNotNull;
import mware_lib.NameService;
import mware_lib.ObjectBroker;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExtraTests {
	
	private NameService ns;
	private ObjectBroker ob;

	@Before
	public void setUp() {
		ob = ObjectBroker.init("lab33", 13037, false);
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
