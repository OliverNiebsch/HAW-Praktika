package lpon.mps.spring.web.boot;

import java.sql.SQLException;

import lpon.mps.config.config.AppConfiguration;
import lpon.mps.spring.web.controller.MpsRestApi;
import lpon.mps.stammdatenadapter.services.ArtikelService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;


@Configuration
@EnableAutoConfiguration
@PropertySource("classpath:application-dump.properties") // this allows us to configure our database via the application.properties file
public class Application {

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
}
