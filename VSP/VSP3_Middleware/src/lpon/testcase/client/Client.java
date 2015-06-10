package lpon.testcase.client;

import lpon.accessor_one.ClassOneImplBase;
import lpon.accessor_one.ClassTwoImplBase;
import lpon.accessor_one.SomeException110;
import lpon.accessor_one.SomeException112;
import lpon.accessor_two.SomeException304;
import lpon.mware_lib.NameService;
import lpon.mware_lib.ObjectBroker;

public class Client {

	private static ObjectBroker middleWare;
	private static NameService ns;
	private static Object viktor;
	private static Object oliver;
	private static Object lars;
	private static ClassOneImplBase accOneClassOne;
	private static ClassTwoImplBase accOneClassTwo;
	private static lpon.accessor_two.ClassOneImplBase accTwoClassOne;
	
	public static void main(String[] args) {
		
		//Fixture SETUP
		middleWare = ObjectBroker.init("141.22.31.178", 13037, true);
		ns = middleWare.getNameService();
		
		viktor = ns.resolve("viktor"); //ClassOneImplBase
		oliver = ns.resolve("oliver"); //ClassTwoImplBase
		lars = ns.resolve("lars"); //ClassOneImplBase2 (2.package)
		
		accOneClassOne = ClassOneImplBase.narrowCast(viktor);
		accOneClassTwo = ClassTwoImplBase.narrowCast(oliver);
		accTwoClassOne = lpon.accessor_two.ClassOneImplBase.narrowCast(lars);
		
		
		test1();
		test2();
		test3();
		test4();
		test5();
		
		middleWare.shutDown();
	}
	
	private static void test1(){
		String para1 = "egal";
		int para2 = 5;
		
		try {			
			String accOneClassOneMethodOne_return = accOneClassOne.methodOne(para1, para2);
			LogAusgabe.printResult(accOneClassOne.getClass().getName(), "viktor", "methodOne", para1, para2, accOneClassOneMethodOne_return);
		} catch (SomeException112 e) {
			LogAusgabe.printError(accOneClassOne.getClass().getName(), "viktor", "methodOne", para1, para2, e.getClass().getName(), e.getMessage());
		}
	}
	
	private static void test2(){
		double para1 = 4;
		try {
			int accOneClassTwoMethodOne_return = accOneClassTwo.methodOne(para1);
			LogAusgabe.printResult(accOneClassTwo.getClass().getName(), "oliver", "methodOne", para1, accOneClassTwoMethodOne_return);
		} catch (SomeException110 e) {
			LogAusgabe.printError(accOneClassTwo.getClass().getName(), "oliver", "methodOne", para1, e.getClass().getName(), e.getMessage());
		}
	}
	
	private static void test3(){
		try {
			double accOneClassTwoMethodTwo_return = accOneClassTwo.methodTwo();
			LogAusgabe.printResult(accOneClassTwo.getClass().getName(), "oliver", "methodTwo", accOneClassTwoMethodTwo_return);
		} catch (SomeException112 e) {
			LogAusgabe.printError(accOneClassTwo.getClass().getName(), "oliver", "methodTwo", e.getClass().getName(), e.getMessage());
		}
	}
	
	private static void test4(){
		String para1 = "TEST";
		double para2 = 3.4;
		try {
			double accTwoClassOneMethodOne_return = accTwoClassOne.methodOne("TEST", 3.4);
			LogAusgabe.printResult(accTwoClassOne.getClass().getName(), "lars", "methodOne", para1, para2, accTwoClassOneMethodOne_return);
			
		} catch (lpon.accessor_two.SomeException112 e) {
			LogAusgabe.printError(accTwoClassOne.getClass().getName(), "lars", "methodOne", para1, para2, e.getClass().getName(), e.getMessage());
		}
	}
	
	private static void test5(){
		String para1 = "12";
		double para2 = -3.4;
		try {
			double accTwoClassOneMethodTwo_return = accTwoClassOne.methodTwo("12", -3.4);
			LogAusgabe.printResult(accTwoClassOne.getClass().getName(), "lars", "methodTwo", para1, para2, accTwoClassOneMethodTwo_return);
		} catch (lpon.accessor_two.SomeException112 e) {
			LogAusgabe.printError(accTwoClassOne.getClass().getName(), "lars", "methodTwo", para1, para2, e.getClass().getName(), e.getMessage());
		} catch (SomeException304 e) {
			LogAusgabe.printError(accTwoClassOne.getClass().getName(), "lars", "methodTwo", para1, para2, e.getClass().getName(), e.getMessage());
		}
	}
	
}
