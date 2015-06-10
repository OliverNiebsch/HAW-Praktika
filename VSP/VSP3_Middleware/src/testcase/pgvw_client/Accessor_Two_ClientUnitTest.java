package testcase.pgvw_client;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static testcase.pgvw_client.werkzeug.printError;
import static testcase.pgvw_client.werkzeug.printResult;
import mware_lib.NameService;
import mware_lib.ObjectBroker;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import accessor_two.ClassOneImplBase;
import accessor_two.SomeException112;
import accessor_two.SomeException304;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Accessor_Two_ClientUnitTest {

	

	private NameService ns;
	private ObjectBroker ob;

	@Before
	public void setUp() {
		ob = ObjectBroker.init("lab33", 13037, false);
		ns = ob.getNameService();

	}

	@Test
	public void accessorTwo_MethodOne_posetiv() throws SomeException112 {
		Object refObject = ns.resolve("accessor_two:ClassTwoImplBase");
		ClassOneImplBase prox = ClassOneImplBase.narrowCast(refObject);

		try {
			double result = prox.methodOne("15", 15.0);
			assertTrue(result == 30.0);
			printResult(prox, "accessor_two:ClassTwoImplBase", "methodOne", result,"15", 15.0 );
		} catch (Exception e) {

		}

	}

	@Test
	public void accessorTwo_MethodOne_negativ() throws SomeException112 {
		Object refObject = ns.resolve("accessor_two:ClassTwoImplBase");
		ClassOneImplBase prox = ClassOneImplBase.narrowCast(refObject);

		Exception exception = null;
		try {
			prox.methodOne("Hallo Ein Test", 15);
		} catch (Exception e) {
			exception = e;
		
		}

		assertNotNull(exception);

		assertThat(exception, instanceOf(SomeException112.class));
		assertTrue(exception.getMessage().contains("param1 keine Zahl"));
		printError(prox,"accessor_two:ClassTwoImplBase","methodOne",exception,"Hallo Ein Test", 15);
		
	}	


	@Test
	public void accessorTwo_MethodOne_negativ2() {
		Object refObject = ns.resolve("accessor_two:ClassTwoImplBase");
		ClassOneImplBase prox = ClassOneImplBase.narrowCast(refObject);

		Exception exception = null;
		try {
			prox.methodOne(null, 15);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(exception);
		assertThat(exception, instanceOf(SomeException112.class));
	
		assertTrue(exception.getMessage().contains("param1==null"));
		printError(prox,"accessor_two:ClassTwoImplBase","methodOne",exception,null, 15);
	}
	
	@Test
	public void accessorTwo_MethodTwo_posetiv() throws SomeException112 {
		Object refObject = ns.resolve("accessor_two:ClassTwoImplBase");
		ClassOneImplBase prox = ClassOneImplBase.narrowCast(refObject);

		try {
			double result = prox.methodTwo("15", 15);
			assertThat(result, is(225.0));
			printResult(prox, "accessor_two:ClassTwoImplBase", "methodTwo", result,"15", 15.0 );
		} catch (Exception e) {

		}

	}
	
	@Test
	public void accessorTwo_MethodTow_negativ1() {
		Object refObject = ns.resolve("accessor_two:ClassTwoImplBase");
		ClassOneImplBase prox = ClassOneImplBase.narrowCast(refObject);

		Exception exception = null;
		try {
			prox.methodTwo("Hallo Ein Test", 15);
		} catch (Exception e) {
			exception = e;
		}

		assertNotNull(exception);

		assertThat(exception, instanceOf(SomeException304.class));
		assertTrue(exception.getMessage().contains("param1 keine Zahl"));
		printError(prox,"accessor_two:ClassTwoImplBase","methodTwo",exception,"Hallo Ein Test", 15);
	}
	
	@Test
	public void accessorTwo_MethodTow_negativ2() {
		Object refObject = ns.resolve("accessor_two:ClassTwoImplBase");
		ClassOneImplBase prox = ClassOneImplBase.narrowCast(refObject);

		Exception exception = null;
		try {
			prox.methodTwo(null, 15);
		} catch (Exception e) {
			exception = e;
		}
		assertNotNull(exception);
		assertThat(exception, instanceOf(SomeException112.class));
	
		assertTrue(exception.getMessage().contains("param1==null"));
		printError(prox,"accessor_two:ClassTwoImplBase","methodTwo",exception,null, 15);

	}

	@After
	public void after() {
		ob.shutDown();
	}
	
	
	



}
