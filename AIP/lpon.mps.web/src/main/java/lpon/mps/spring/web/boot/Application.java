package lpon.mps.spring.web.boot;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Scanner;

import lpon.mps.config.config.AppConfiguration;
import lpon.mps.spring.web.controller.MpsRestApi;
import lpon.mps.stammdatenadapter.services.ArtikelService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:application-dump.properties") // this allows us to configure our database via the application.properties file
public class Application {
	private static final String dispatcher = "http://localhost:8085";
	
	private int port = 8080;
	public void setPort(int port) {this.port = port;}
	public int getPort(){return this.port;}

	// This is necessary to programmatically define the port of the application
	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		System.out.println("Starting server at port: "+port);
		return new JettyEmbeddedServletContainerFactory(getPort());
	}
	
	private class AliveThread extends Thread {
		private String port;
		
		public AliveThread(String port) {
			this.port = port;
		}
		
		@Override
		public void run() {
			try {
				while(!isInterrupted()) {
					Scanner response = new Scanner(new URL(dispatcher + "/imalive?port=" + port).openStream());
					
					System.out.println("Sending Dispatcher \"I'm alive\". Getting:");
					while (response.hasNextLine()) {
						System.out.println(response.nextLine());
					}
					
					response.close();
					
					Thread.sleep(10000);
				}
			} catch (InterruptedException e) {
				// TODO: handle exception
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
    public static void main(String[] args) throws SQLException {

    	ConfigurableApplicationContext ctx = SpringApplication.run(
    			new Object[]{
    					Application.class, // the application
    					AppConfiguration.class, // the configuration of this application services and entities (see spring.services)
    					TestDataInitializerWeb.class, // the data population
    					MpsRestApi.class // the main controller to supply the rest interface to the outside world
    			}, args); 
  	        
    	// Through this you can test if beans are available and 
    	// what result they return.
    	ArtikelService us = ctx.getBean(ArtikelService.class);
  		System.out.println("users with 's': "+us.getArtikel("s"));
    }
    
    public Application() {
    	// I'm alive sender
    	new AliveThread(new Integer(port).toString()).start();
    }
}
