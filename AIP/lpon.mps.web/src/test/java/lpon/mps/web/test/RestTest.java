package lpon.mps.web.test;

import lpon.mps.stammdatenadapter.entities.Artikel;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class RestTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(RestTest.class);

	private static final String URL1 		= "http://localhost:8080";
	private static final String URL2 		= "http://localhost:8081";

    
	@Test
	public void findArtikelById(){
		LOGGER.debug("start test");
		System.out.println("findArtikelById");
		RestTemplate restTemplate = new RestTemplate();
    	Artikel u = restTemplate.getForObject(URL1+"/users/{id}", Artikel.class, 1L);
    	System.out.println(u);
    	System.out.println("2");
    	u = restTemplate.getForObject(URL2+"/users/{id}", Artikel.class, 1L);
    	//u = restTemplate.getForObject(url, responseType)
    	System.out.println(u);
	}
	
    @Test
    public void findUserById() {
    	LOGGER.debug("start test");
    	RestTemplate restTemplate = new RestTemplate();
    	System.out.println("1");
    	Artikel u = restTemplate.getForObject(URL1+"/users/{id}", Artikel.class, 1L);
    	System.out.println(u);
    	System.out.println("2");
    	u = restTemplate.getForObject(URL2+"/users/{id}", Artikel.class, 1L);
    	//u = restTemplate.getForObject(url, responseType)
    	System.out.println(u);
    }
}
