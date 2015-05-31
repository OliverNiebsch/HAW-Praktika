package lpon.mps.stammdatenadapter;

import lpon.mps.stammdatenadapter.entities.Kunde;
import lpon.mps.stammdatenadapter.repositories.KundeRepository;
import lpon.mps.stammdatenadapter.services.KundeService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=lpon.mps.stammdatenadapter.KundenTest.ContextConfiguration.class)
public class KundenTest {

	@ComponentScan(basePackages = "lpon.mps.stammdatenadapter")
	@Configuration
	static class ContextConfiguration {}
	
	@Autowired
	private KundeService kundeService;
	
	@Autowired
	private KundeRepository kundeRepository;
	
	@Test
	public void findKunde() {
		Kunde k1 = new Kunde();
		k1 = kundeRepository.save(k1);
		
		Kunde k2 = new Kunde();
		k2 = kundeRepository.save(k2);
		
		// search
		Assert.notNull(kundeService.getKunde(k1.getId()));
		Assert.notNull(kundeService.getKunde(k2.getId()));
		Assert.isNull(kundeService.getKunde(k1.getId() * 10));
	}
}
