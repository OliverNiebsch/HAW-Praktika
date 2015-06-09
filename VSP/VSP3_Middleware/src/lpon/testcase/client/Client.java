package lpon.testcase.client;

import lpon.accessor_one.ClassOneImplBase;
import lpon.accessor_one.ClassTwoImplBase;
import lpon.accessor_one.SomeException110;
import lpon.accessor_one.SomeException112;
import lpon.accessor_two.SomeException304;
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
			String return11 = bla.methodOne("egal", 5);
			System.out.println("11: Returned: " + return11);
		} catch (SomeException112 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			int return12 = bla2.methodOne(4);
			System.out.println("12: Returned: " + return12);
		} catch (SomeException110 e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			Double return21 = bla2.methodTwo();
			System.out.println("21: Returned: " + return21);
		} catch (SomeException112 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			Double return31 = bla3.methodOne("TEST", 3.4);
			System.out.println("31: Returned: " + return31);
		} catch (lpon.accessor_two.SomeException112 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Double return32 = bla3.methodTwo("12", -3.4);
			System.out.println("32: Returned: " + return32);
		} catch (lpon.accessor_two.SomeException112 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SomeException304 e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		middleWare.shutDown();
	}
}
