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
		middleWare = ObjectBroker.init("lab33", 13037, true);
		ns = middleWare.getNameService();
		
		viktor = ns.resolve("viktor1"); //ClassOneImplBase
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
		String para1;
		int para2;

		//POSITIV FALL
		para1 = "egal";
		para2 = 5;
		try {			
			String accOneClassOneMethodOne_return = accOneClassOne.methodOne(para1, para2);
			LogAusgabe.printResult(accOneClassOne.getClass().getName(), "viktor", "methodOne", para1, para2, accOneClassOneMethodOne_return);
		} catch (SomeException112 e) {
			LogAusgabe.printError(accOneClassOne.getClass().getName(), "viktor", "methodOne", para1, para2, e.getClass().getName(), e.getMessage());
		}
		
		//EXCEPTION
		para1 = "egal";
		para2 = -5;
		try {			
			String accOneClassOneMethodOne_return = accOneClassOne.methodOne(para1, para2);
			LogAusgabe.printResult(accOneClassOne.getClass().getName(), "viktor", "methodOne", para1, para2, accOneClassOneMethodOne_return);
		} catch (SomeException112 e) {
			LogAusgabe.printError(accOneClassOne.getClass().getName(), "viktor", "methodOne", para1, para2, e.getClass().getName(), e.getMessage());
		}
		
		//NULL
		para1 = null;
		para2 = 4;
		try {			
			String accOneClassOneMethodOne_return = accOneClassOne.methodOne(para1, para2);
			LogAusgabe.printResult(accOneClassOne.getClass().getName(), "viktor", "methodOne", para1, para2, accOneClassOneMethodOne_return);
		} catch (SomeException112 e) {
			LogAusgabe.printError(accOneClassOne.getClass().getName(), "viktor", "methodOne", para1, para2, e.getClass().getName(), e.getMessage());
		}
	}
	
	private static void test2(){
		double para1;
		
		//POSITIV FALL
		para1 = 4;
		try {
			int accOneClassTwoMethodOne_return = accOneClassTwo.methodOne(para1);
			LogAusgabe.printResult(accOneClassTwo.getClass().getName(), "oliver", "methodOne", para1, accOneClassTwoMethodOne_return);
		} catch (SomeException110 e) {
			LogAusgabe.printError(accOneClassTwo.getClass().getName(), "oliver", "methodOne", para1, e.getClass().getName(), e.getMessage());
		}
		
		//EXCEPTION
		para1 = -4;
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
		String para1;
		double para2;

		//SUCCESS
		para1 = "1.0";
		para2 = 3.4;
		try {
			double accTwoClassOneMethodOne_return = accTwoClassOne.methodOne(para1, para2);
			LogAusgabe.printResult(accTwoClassOne.getClass().getName(), "lars", "methodOne", para1, para2, accTwoClassOneMethodOne_return);
			
		} catch (lpon.accessor_two.SomeException112 e) {
			LogAusgabe.printError(accTwoClassOne.getClass().getName(), "lars", "methodOne", para1, para2, e.getClass().getName(), e.getMessage());
		}
		
		//EXCEPTION1
		para1 = "EXCEPTION1";
		para2 = 3.4;
		try {
			double accTwoClassOneMethodOne_return = accTwoClassOne.methodOne(para1, para2);
			LogAusgabe.printResult(accTwoClassOne.getClass().getName(), "lars", "methodOne", para1, para2, accTwoClassOneMethodOne_return);
			
		} catch (lpon.accessor_two.SomeException112 e) {
			LogAusgabe.printError(accTwoClassOne.getClass().getName(), "lars", "methodOne", para1, para2, e.getClass().getName(), e.getMessage());
		}
		
		//EXCEPTION1
		para1 = null;
		para2 = 3.2;
		try {
			double accTwoClassOneMethodOne_return = accTwoClassOne.methodOne(para1, para2);
			LogAusgabe.printResult(accTwoClassOne.getClass().getName(), "lars", "methodOne", para1, para2, accTwoClassOneMethodOne_return);
			
		} catch (lpon.accessor_two.SomeException112 e) {
			LogAusgabe.printError(accTwoClassOne.getClass().getName(), "lars", "methodOne", para1, para2, e.getClass().getName(), e.getMessage());
		}
	}
	
	private static void test5(){
		String para1;
		double para2;
		
		para1 = "3.1";
		para2 = 3.4;
		try {
			double accTwoClassOneMethodTwo_return = accTwoClassOne.methodTwo(para1, para2);
			LogAusgabe.printResult(accTwoClassOne.getClass().getName(), "lars", "methodTwo", para1, para2, accTwoClassOneMethodTwo_return);
		} catch (lpon.accessor_two.SomeException112 e) {
			LogAusgabe.printError(accTwoClassOne.getClass().getName(), "lars", "methodTwo", para1, para2, e.getClass().getName(), e.getMessage());
		} catch (SomeException304 e) {
			LogAusgabe.printError(accTwoClassOne.getClass().getName(), "lars", "methodTwo", para1, para2, e.getClass().getName(), e.getMessage());
		}
		
		para1 = null;
		para2 = 3.1;
		try {
			double accTwoClassOneMethodTwo_return = accTwoClassOne.methodTwo(para1, para2);
			LogAusgabe.printResult(accTwoClassOne.getClass().getName(), "lars", "methodTwo", para1, para2, accTwoClassOneMethodTwo_return);
		} catch (lpon.accessor_two.SomeException112 e) {
			LogAusgabe.printError(accTwoClassOne.getClass().getName(), "lars", "methodTwo", para1, para2, e.getClass().getName(), e.getMessage());
		} catch (SomeException304 e) {
			LogAusgabe.printError(accTwoClassOne.getClass().getName(), "lars", "methodTwo", para1, para2, e.getClass().getName(), e.getMessage());
		}
		
		para1 = "EXCEPTION1";
		para2 = 3.4;
		try {
			double accTwoClassOneMethodTwo_return = accTwoClassOne.methodTwo(para1, para2);
			LogAusgabe.printResult(accTwoClassOne.getClass().getName(), "lars", "methodTwo", para1, para2, accTwoClassOneMethodTwo_return);
		} catch (lpon.accessor_two.SomeException112 e) {
			LogAusgabe.printError(accTwoClassOne.getClass().getName(), "lars", "methodTwo", para1, para2, e.getClass().getName(), e.getMessage());
		} catch (SomeException304 e) {
			LogAusgabe.printError(accTwoClassOne.getClass().getName(), "lars", "methodTwo", para1, para2, e.getClass().getName(), e.getMessage());
		}
		
		para1 = "1.0";
		para2 = -3.4;
		try {
			double accTwoClassOneMethodTwo_return = accTwoClassOne.methodTwo(para1, para2);
			LogAusgabe.printResult(accTwoClassOne.getClass().getName(), "lars", "methodTwo", para1, para2, accTwoClassOneMethodTwo_return);
		} catch (lpon.accessor_two.SomeException112 e) {
			LogAusgabe.printError(accTwoClassOne.getClass().getName(), "lars", "methodTwo", para1, para2, e.getClass().getName(), e.getMessage());
		} catch (SomeException304 e) {
			LogAusgabe.printError(accTwoClassOne.getClass().getName(), "lars", "methodTwo", para1, para2, e.getClass().getName(), e.getMessage());
		}
	}
	
}
