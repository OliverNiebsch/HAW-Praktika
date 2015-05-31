package lpon.mps.stammdatenadapter;

import java.util.ArrayList;

import lpon.mps.stammdatenadapter.entities.Artikel;
import lpon.mps.stammdatenadapter.services.ArtikelService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=lpon.mps.stammdatenadapter.ArtikelTest.ContextConfiguration.class)
public class ArtikelTest {

	@ComponentScan(basePackages = "lpon.mps.stammdatenadapter")
	@Configuration
	static class ContextConfiguration {}
	
	@Autowired
	private ArtikelService artikelService;
	
	@Test
	public void createArtikel() {
		Artikel a1 = new Artikel("Schraube", null);
		Assert.isNull(a1.getId());
		artikelService.saveArtikel(a1);
		Assert.notNull(a1.getId());
		
		Artikel a2 = new Artikel("Mutter", null);
		Assert.isNull(a2.getId());
		artikelService.saveArtikel(a2);
		Assert.notNull(a2.getId());
		
		ArrayList<Artikel> artikelListe = new ArrayList<Artikel>();
		artikelListe.add(a1);
		artikelListe.add(a2);
		
		Artikel a3 = new Artikel("Rasenmäher", artikelListe);
		Assert.isNull(a3.getId());
		artikelService.saveArtikel(a3);
		Assert.notNull(a3.getId());
	}
}
