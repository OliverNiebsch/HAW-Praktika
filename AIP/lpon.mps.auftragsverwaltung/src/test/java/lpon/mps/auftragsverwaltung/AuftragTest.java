package lpon.mps.auftragsverwaltung;

import java.util.ArrayList;
import java.util.List;

import lpon.mps.auftragsverwaltung.entities.Angebot;
import lpon.mps.auftragsverwaltung.entities.Auftrag;
import lpon.mps.auftragsverwaltung.services.AngebotService;
import lpon.mps.auftragsverwaltung.services.AuftragService;
import lpon.mps.stammdatenadapter.entities.Artikel;
import lpon.mps.stammdatenadapter.entities.Kunde;
import lpon.mps.stammdatenadapter.repositories.KundeRepository;
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
@ContextConfiguration(classes=lpon.mps.auftragsverwaltung.AuftragTest.ContextConfiguration.class)
public class AuftragTest {
	// TODO: Mocking einbauen, sodass nur Komponenten aus dem lpon.mps.auftragsverwaltungs Package verwendet werden 
	
	
	@ComponentScan(basePackages = {"lpon.mps.stammdatenadapter", "lpon.mps.auftragsverwaltung"})
	@Configuration
	static class ContextConfiguration {}
	
	@Autowired
	private KundeRepository kundenRepo;
	
	@Autowired
	private ArtikelService artikelService;
	
	@Autowired
	private AuftragService auftragService;
	@Autowired
	private AngebotService angebotService;
	
	private Angebot ang1;
	
	@Test
	public void createAndFindAngebot() {
		Kunde k1 = new Kunde();
		kundenRepo.save(k1);
		
		Artikel a1 = new Artikel("Artikel1", null);
		artikelService.saveArtikel(a1);
		
		List<Artikel> artikelListe = new ArrayList<Artikel>();
		artikelListe.add(a1);
		
		ang1 = new Angebot(k1, artikelListe);
		ang1 = angebotService.saveAngebot(ang1);
		
		Assert.notNull(ang1.getId());
		Angebot founded = angebotService.getAngebot(ang1.getId());
		Assert.notNull(founded);
		Assert.isTrue(ang1.equals(founded));
		
		Assert.isNull(angebotService.getAngebot(ang1.getId() * 10));
	}
	
	@Test
	public void createAndFindAuftrag() {
		Auftrag auf1 = auftragService.createAuftrag(ang1);
		Assert.notNull(auf1);
		Assert.notNull(auf1.getId());
		
		Auftrag founded = auftragService.getAuftrag(auf1.getId());
		Assert.notNull(founded);
		Assert.isTrue(auf1.equals(founded));
		
		Assert.isNull(auftragService.getAuftrag(10));
	}
}
