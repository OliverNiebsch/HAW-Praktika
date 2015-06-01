package lpon.mps.fertigung;


import java.util.ArrayList;

import lpon.mps.auftragsverwaltung.entities.Angebot;
import lpon.mps.auftragsverwaltung.entities.Auftrag;
import lpon.mps.auftragsverwaltung.services.AngebotService;
import lpon.mps.auftragsverwaltung.services.AuftragService;
import lpon.mps.fertigung.entities.Fertigungsauftrag;
import lpon.mps.fertigung.services.FertigungsauftragService;
import lpon.mps.stammdatenadapter.entities.Artikel;
import lpon.mps.stammdatenadapter.entities.Kunde;
import lpon.mps.stammdatenadapter.repositories.KundeRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=lpon.mps.fertigung.FertigungsauftragsTest.ContextConfiguration.class)
public class FertigungsauftragsTest {

	@ComponentScan(basePackages = {"lpon.mps.stammdatenadapter","lpon.mps.auftragsverwaltung","lpon.mps.fertigung"})
	@Configuration
	static class ContextConfiguration {}
	
	@Autowired
	private KundeRepository kundenRepo;
	
	@Autowired
	private AngebotService angebotService;
	
	@Autowired
	private AuftragService auftragService;
	
	@Autowired
	private FertigungsauftragService fertigungsauftragService;
	
	@Test
	public void Test() {
		Kunde k = new Kunde();
		k = kundenRepo.save(k);
		
		Angebot angebot = new Angebot(k, new ArrayList<Artikel>());
		angebot = angebotService.saveAngebot(angebot);
		
		Auftrag auftrag = auftragService.createAuftrag(angebot);
		
		Fertigungsauftrag fertAuftr = fertigungsauftragService.createFertigungsauftrag(auftrag);
		
		Assert.notNull(fertAuftr);
		System.out.println(fertAuftr);
	}
}
