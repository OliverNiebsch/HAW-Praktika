package testcase.server;

import java.io.IOException;
import java.io.InputStreamReader;

import mware_lib.NameService;
import mware_lib.ObjectBroker;

public class Server {
	private static final String host = "lab33";
	private static final int port = 13037;

	public static void main(String[] args) throws IOException {
		String ns_host;
		int ns_port;
		
		if (args.length != 2) {
			ns_host = args[0];
			ns_port = Integer.parseInt(args[1]);
		} else {
			ns_host = host;
			ns_port = port;
		}
		
		AccessorOneClassOne a1c1 = new AccessorOneClassOne();
		AccessorOneClassTwo a1c2 = new AccessorOneClassTwo();
		AccessorTwoClassOne a2c1 = new AccessorTwoClassOne();
		
		ObjectBroker mware = ObjectBroker.init(ns_host, ns_port, true);
		NameService nService = mware.getNameService();
		
		nService.rebind(a1c1, "viktor");
		nService.rebind(a1c2, "oliver");
		nService.rebind(a2c1, "lars");
		
		InputStreamReader reader = new InputStreamReader(System.in);
		reader.read();
		
		mware.shutDown();
	}

}
