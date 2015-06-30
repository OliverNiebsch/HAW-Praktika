package lpon.mps.spring.web.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MpsDispatcherApi {
	private class Server {
		private String host, port;
		private long lastAlive;
		
		public Server(String host, String port) {
			this.host = host;
			this.port = port;
			this.lastAlive = new Date().getTime();
		}

		public boolean isAlive() {
			return (new Date().getTime() - lastAlive) < 60000;
		}

		public void refreshLastAlive() {
			System.out.println(host + ":" + port + " is alive!");
			this.lastAlive = new Date().getTime();
		}

		public String getHost() {
			return host;
		}

		public String getPort() {
			return port;
		}
	}
	
	private Server server1 = new Server("127.0.0.1", "8080"), server2 = new Server("127.0.0.1", "8081");
	private boolean first = true;
	
	private void redirect(HttpServletResponse response, String newUrl) {
		response.setStatus(307);
        response.setHeader("Location", newUrl);
        response.setHeader("Connection", "close");
	}
	
	@RequestMapping(value = "/auftrag", method = RequestMethod.POST)
	public void auftragAnlegen(@RequestParam(value="angebot", required=true) Long angebotId, HttpServletRequest request, HttpServletResponse response) {
		if ((first && server1.isAlive()) || !server2.isAlive()) {
			redirect(response, "http://" + server1.getHost() + ":" + server1.getPort() + "/auftrag");
		} else {
			redirect(response, "http://" + server2.getHost() + ":" + server2.getPort() + "/auftrag");
		}
		
		first = !first;
	}

	@RequestMapping(value = "/auftrag", method = RequestMethod.GET)
	public void getAuftraegeFuerKunde(@RequestParam(value="kunde", required=true) Long kundeId, HttpServletRequest request, HttpServletResponse response) {
		if ((first && server1.isAlive()) || !server2.isAlive()) {
			redirect(response, "http://" + server1.getHost() + ":" + server1.getPort() + "/auftrag");
		} else {
			redirect(response, "http://" + server2.getHost() + ":" + server2.getPort() + "/auftrag");
		}
		
		first = !first;
	}
	
	@RequestMapping(value = "/imalive", method = RequestMethod.GET)
	public void imAliveMsg(@RequestParam(value="port", required=true) String port, HttpServletRequest request, HttpServletResponse response) {
		String host = request.getRemoteHost();
		
		if (server1.getHost().equals(host) && server1.getPort().equals(port)) {
			server1.refreshLastAlive();
		} else if (server2.getHost().equals(host) && server2.getPort().equals(port)) {
			server2.refreshLastAlive();
		}		
	}
}