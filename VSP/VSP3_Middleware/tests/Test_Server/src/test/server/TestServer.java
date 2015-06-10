package test.server;

import test.server.accessor_one.Accessor_One_ServerClassOneImpl;
import test.server.accessor_one.Accessor_One_ServerClassTwoImpl;
import test.server.accessor_two.Accessor_Two_ServerClaseOneImpl;
import mware_lib.NameService;
import mware_lib.ObjectBroker;
import mware_lib.util.PropUtil;

public class TestServer {

	public TestServer() {
		// Abbildung 2 Punkt 2 und Punkt 4
		ObjectBroker ob = ObjectBroker.init(PropUtil.getProp("nameserverHost"), PropUtil.getIntProp("nameserverPort"), true);
		
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
		 new TestServer();
	}

}
