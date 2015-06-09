package lpon.testcase.client;

import lpon.accessor_one.ClassOneImplBase;
import lpon.accessor_one.ClassTwoImplBase;
import lpon.accessor_one.SomeException112;
import lpon.mware_lib.NameService;
import lpon.mware_lib.ObjectBroker;

public class Client {

	
	
	public static void main(String[] args) {
		
		ObjectBroker middleWare = ObjectBroker.init("141.22.31.178", 13037, true);
		
		NameService ns = middleWare.getNameService();
		
		Object viktor = ns.resolve("viktor"); //ClassOneImplBase
		Object oliver = ns.resolve("oliver"); //ClassTwoImplBase
		Object lars = ns.resolve("lars"); //ClassOneImplBase2 (2.package)
		
		ClassOneImplBase bla = ClassOneImplBase.narrowCast(viktor);
		ClassTwoImplBase bla2 = ClassTwoImplBase.narrowCast(oliver);
		lpon.accessor_two.ClassOneImplBase bla3 = lpon.accessor_two.ClassOneImplBase.narrowCast(lars);
		
		
		try {
			bla.methodOne("egal", 5);
		} catch (SomeException112 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		middleWare.shutDown();
	}

}
