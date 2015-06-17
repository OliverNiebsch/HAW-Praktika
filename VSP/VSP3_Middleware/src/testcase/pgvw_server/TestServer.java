package testcase.pgvw_server;
import mware_lib.NameService;
import mware_lib.ObjectBroker;
import testcase.pgvw_server.accessor_one.Accessor_One_ServerClassOneImpl;
import testcase.pgvw_server.accessor_one.Accessor_One_ServerClassTwoImpl;
import testcase.pgvw_server.accessor_two.Accessor_Two_ServerClaseOneImpl;

public class TestServer {
	
	private static final int DEFAULT_NAMESERVER_PORT = 9988;
	private static final String DEFAULT_NAMESERVER_HOST = "127.0.0.1";


	public TestServer(String nameserverHostname, int nameserverPort) {
		// Abbildung 2 Punkt 2 und Punkt 4
		ObjectBroker ob = ObjectBroker.init(nameserverHostname,nameserverPort, true);
		
		// Abbildung 2 Punkt 5
		NameService ns = ob.getNameService();
		
		
		// Abbildung 2 Punkt 6,
		Accessor_One_ServerClassOneImpl classOneImplBase = new Accessor_One_ServerClassOneImpl();
		ns.rebind(classOneImplBase, "accessor_one:ClassOneImplBase");
		
		Accessor_One_ServerClassTwoImpl classTwoImplBase = new Accessor_One_ServerClassTwoImpl();
		ns.rebind(classTwoImplBase, "accessor_one:ClassTwoImplBase");
		
		
		Accessor_Two_ServerClaseOneImpl accessor_two_classTwoImplBase = new Accessor_Two_ServerClaseOneImpl();
		ns.rebind(accessor_two_classTwoImplBase, "accessor_two:ClassTwoImplBase");
		
		ns.rebind(null, "Null");
		
		
	}
	
	public static void main(String[] args) {
		
		if (args.length == 2)
			new TestServer(args[0],Integer.parseInt(args[1]));
		else
			new TestServer(DEFAULT_NAMESERVER_HOST,DEFAULT_NAMESERVER_PORT);

	}

}
