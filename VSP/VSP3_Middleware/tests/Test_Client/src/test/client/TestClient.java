package test.client;

import mware_lib.NameService;
import mware_lib.ObjectBroker;
import mware_lib.ref_object.RefObject;
import mware_lib.util.PropUtil;
import accessor_one.ClassOneImplBase;

public class TestClient {

	private NameService ns;

	
	public TestClient() {
		// Abbildung 3 Punkt 2 und Punkt 4
		ObjectBroker ob = ObjectBroker.init(PropUtil.getProp("nameserverHost"), PropUtil.getIntProp("nameserverPort"), true);
		// Abbildung 3 Punkt 5
		ns = ob.getNameService();
		testSequentiell();
		testMultiThreading();
		ob.shutDown();
	}
	
	private void extraTests(){
		RefObject refObject = ns.resolve("Null");
		ClassOneImplBase prox = ClassOneImplBase.narrowCast(refObject);

		try {
			// Abbildung 4 Punkt 2
			prox.methodOne("15", 500);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private void testSequentiell() {
		// Abbildung 3 Punkt 6
		Object refObject = ns.resolve("accessor_one:ClassOneImplBase");
		// Abbildung 3 Punkt 6.2
		ClassOneImplBase prox = ClassOneImplBase.narrowCast(refObject);

		for (int i = 0; i < 10; i++) {
			String result = null;
			try {
				// Abbildung 4 Punkt 2
				result = prox.methodOne("15", 500);

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

			System.out.println(result);

		}

	}

	private void testMultiThreading() {
		int threadcount = 20;

		Thread[] threads = new Thread[threadcount];
		// Abbildung 3 Punkt 6
		Object refObject = ns.resolve("accessor_one:ClassOneImplBase");
		// Abbildung 3 Punkt 6.2
		ClassOneImplBase prox = ClassOneImplBase.narrowCast(refObject);
		for (int i = 0; i < threadcount; i++) {
			threads[i] = new Thread(new Runnable() {

				@Override
				public void run() {

					String result = null;
					try {
						result = prox.methodOne("15", 5000);

					} catch (Exception e) {
						System.out.println(e.getMessage());
					}

					System.out.println(result);
				}
			});
			threads[i].start();

		}

	}

	public static void main(String[] args) {
		new TestClient();

	}

}
