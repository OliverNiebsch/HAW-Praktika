package lpon.mps.spring.web.boot;

import java.sql.SQLException;

import lpon.mps.config.config.AppConfiguration;
import lpon.mps.spring.web.controller.MpsDispatcherApi;
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
public class Dispatcher {
	
	private int port = 8085;
	public void setPort(int port) {this.port = port;}
	public int getPort(){return this.port;}

	// This is necessary to programmatically define the port of the application
	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		System.out.println("Starting server at port: "+port);
		return new JettyEmbeddedServletContainerFactory(getPort());
	}
	
    public static void main(String[] args) throws SQLException {

    	ConfigurableApplicationContext ctx = SpringApplication.run(
    			new Object[]{
    					Dispatcher.class, // the application
//    					AppConfiguration.class, // the configuration of this application services and entities (see spring.services)
//    					TestDataInitializerWeb.class, // the data population
    					MpsDispatcherApi.class // the main controller to supply the rest interface to the outside world
    			}, args);   	        
    }
}
