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
import mware_lib.util.PropUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import accessor_one.ClassOneImplBase;
import accessor_one.ClassTwoImplBase;
import accessor_one.SomeException110;
import accessor_one.SomeException112;
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Accessor_One_ClientUnitTest {

	private NameService ns;
	private ObjectBroker ob;

	@Before
	public void setUp() {
		ob = ObjectBroker.init(PropUtil.getProp("nameserverHost"), PropUtil.getIntProp("nameserverPort"), false);
		
		ns = ob.getNameService();

	}

	@After
	public void after() {
		ob.shutDown();
	}

	@Test
	public void accessorOne_ClassOne_MethodOne_positiv1() throws SomeException112 {
		Object refObject = ns.resolve("accessor_one:ClassOneImplBase");
		ClassOneImplBase prox = ClassOneImplBase.narrowCast(refObject);

		String result = null;
		try {
			result = prox.methodOne("15", 15);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		assertThat(result, is("30"));
		printResult(prox, "accessor_one:ClassOneImplBase", "methodOne", result,
				"15", 15);
	}

	@Test
	public void accessorOne_ClassOne_MethodOne_negativ1() throws SomeException112 {
		Object refObject = ns.resolve("accessor_one:ClassOneImplBase");
		ClassOneImplBase prox = ClassOneImplBase.narrowCast(refObject);

		Exception exception = null;
		try {
			prox.methodOne(null, 15);

		} catch (Exception e) {
			exception = e;
		}

		assertNotNull(exception);
		assertThat(exception, instanceOf(SomeException112.class));
		assertTrue(exception.getMessage().contains("param1 keine Zahl"));
		printError(prox, "accessor_one:ClassOneImplBase", "methodOne",
				exception, "Hallo Ein Test", 15);

	}

	@Test
	public void accessorOne_ClassTwo_MethodOne_positiv(){
		Object refObject = ns.resolve("accessor_one:ClassTwoImplBase");
		ClassTwoImplBase prox = ClassTwoImplBase.narrowCast(refObject);

	
		int result = 0;
		try {
			result =prox.methodOne(5);

		} catch (Exception e) {
			
		}

		assertThat(result, is(50));
		printResult(prox, "accessor_one:ClassTwoImplBase", "methodOne", result,
				50);

	}
	
	@Test
	public void accessorOne_ClassTwo_MethodOne_negativ(){
		Object refObject = ns.resolve("accessor_one:ClassTwoImplBase");
		ClassTwoImplBase prox = ClassTwoImplBase.narrowCast(refObject);

		Exception exception = null;
		try {
			prox.methodOne(-5);

		} catch (Exception e) {
			exception = e;
		}

		assertNotNull(exception);
		assertThat(exception, instanceOf(SomeException110.class));
		assertTrue(exception.getMessage().contains("Zu klein"));
		printError(prox, "accessor_one:ClassTwoImplBase", "methodOne",
				exception,-5);

	}
	
	@Test
	public void accessorOne_ClassTwo_MethodTwo_negativ(){
		Object refObject = ns.resolve("accessor_one:ClassTwoImplBase");
		ClassTwoImplBase prox = ClassTwoImplBase.narrowCast(refObject);

		Exception exception = null;
		try {
			prox.methodTwo();

		} catch (Exception e) {
			exception = e;
		}

		assertNotNull(exception);
		assertThat(exception, instanceOf(SomeException112.class));
		assertTrue(exception.getMessage().contains("Erste Ausf�hrung f�hrt zu Fehler!"));
		printError(prox, "accessor_one:ClassTwoImplBase", "methodTwo",
				exception,-5);

	}
	
	
	
	@Test
	public void accessorOne_ClassTwo_MethodTwo_positiv(){
		Object refObject = ns.resolve("accessor_one:ClassTwoImplBase");
		ClassTwoImplBase prox = ClassTwoImplBase.narrowCast(refObject);

	
		double result = 0;
		try {
			result =prox.methodTwo();

		} catch (Exception e) {
			
		}

		assertThat(result, is(999.9999));
		printResult(prox, "accessor_one:ClassTwoImplBase", "methodTwo", result);

	}
}
