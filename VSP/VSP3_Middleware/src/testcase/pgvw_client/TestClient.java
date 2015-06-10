package testcase.pgvw_client;

import mware_lib.NameService;
import mware_lib.ObjectBroker;
import accessor_one.ClassOneImplBase;

public class TestClient {

	private static final int DEFAULT_NAMESERVER_PORT = 9988;
	private static final String DEFAULT_NAMESERVER_HOST = "127.0.0.1";
	private NameService ns;

	public TestClient(String nameserverHost, int nameserverPort) {
		// Abbildung 3 Punkt 2 und Punkt 4
		ObjectBroker ob = ObjectBroker.init(nameserverHost, nameserverPort,
				true);
		// Abbildung 3 Punkt 5
		ns = ob.getNameService();
		testSequentiell();
		testMultiThreading();
		ob.shutDown();
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

		if (args.length == 2)
			new TestClient(args[0], Integer.parseInt(args[1]));
		else
			new TestClient(DEFAULT_NAMESERVER_HOST, DEFAULT_NAMESERVER_PORT);

	}

}
