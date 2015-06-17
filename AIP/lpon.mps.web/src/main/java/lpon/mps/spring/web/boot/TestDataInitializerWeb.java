package lpon.mps.spring.web.boot;

import java.util.Arrays;

import lpon.mps.auftragsverwaltung.entities.Angebot;
import lpon.mps.auftragsverwaltung.repositories.AngebotRepository;
import lpon.mps.stammdatenadapter.entities.Artikel;
import lpon.mps.stammdatenadapter.entities.Kunde;
import lpon.mps.stammdatenadapter.repositories.ArtikelRepository;
import lpon.mps.stammdatenadapter.repositories.KundeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class TestDataInitializerWeb implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestDataInitializerWeb.class);

	@Autowired
	private ArtikelRepository artikelRepository;

	@Autowired
	private KundeRepository kundeRepository;
	
	@Autowired
	private AngebotRepository angebotRepository;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// startup of root context, refresh will trigger for initialization and refresh of context
		if (event.getApplicationContext().getParent() == null) {
			configure();
		}
	}

	private void configure() {
		LOGGER.info("setup db.");
		Artikel artikel1 = artikelRepository.save(new Artikel("Schraube", null));
		Artikel artikel2 = artikelRepository.save(new Artikel("Nupsi", null));
		Artikel artikel3 = artikelRepository.save(new Artikel("Metall", null));
		Artikel artikel4 = artikelRepository.save(new Artikel("Klinge", null));
		Artikel artikel5 = artikelRepository.save(new Artikel("Motor", null));
		
		Artikel rasenmaeher = artikelRepository.save(new Artikel("Rasenmäher", Arrays.asList(new Artikel[]{artikel1, artikel3, artikel4, artikel5})));
		Artikel messer = artikelRepository.save(new Artikel("Küchenmesser", Arrays.asList(new Artikel[]{artikel1, artikel2, artikel5})));
		
		Kunde kundeHans = kundeRepository.save(new Kunde("Hans", "Hamburg"));
		Kunde kundeDieter = kundeRepository.save(new Kunde("Dieter", "Dortmund"));
		Kunde kundeNadine = kundeRepository.save(new Kunde("Nadine", "Nürnberg"));
		
		Angebot ang1 = angebotRepository.save(new Angebot(kundeNadine, Arrays.asList(new Artikel[]{rasenmaeher})));
		Angebot ang2 = angebotRepository.save(new Angebot(kundeDieter, Arrays.asList(new Artikel[]{artikel5, rasenmaeher})));
		Angebot ang3 = angebotRepository.save(new Angebot(kundeHans, Arrays.asList(new Artikel[]{messer, artikel2})));
		Angebot ang4 = angebotRepository.save(new Angebot(kundeHans, Arrays.asList(new Artikel[]{artikel1, artikel2})));
	}
}
