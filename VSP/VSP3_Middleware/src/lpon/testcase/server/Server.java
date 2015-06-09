package lpon.testcase.server;

import java.io.IOException;
import java.io.InputStreamReader;

import lpon.mware_lib.NameService;
import lpon.mware_lib.ObjectBroker;

public class Server {

	public static void main(String[] args) throws IOException {
		AccessorOneClassOne a1c1 = new AccessorOneClassOne();
		AccessorOneClassTwo a1c2 = new AccessorOneClassTwo();
		AccessorTwoClassOne a2c1 = new AccessorTwoClassOne();
		
		ObjectBroker mware = ObjectBroker.init("141.22.31.178", 13037, true);
//		ObjectBroker mware = ObjectBroker.init("localhost", 13037, true);
		NameService nService = mware.getNameService();
		
		nService.rebind(a1c1, "viktor");
		nService.rebind(a1c2, "oliver");
		nService.rebind(a2c1, "lars");
		
		InputStreamReader reader = new InputStreamReader(System.in);
		reader.read();
		
		mware.shutDown();
	}

}
