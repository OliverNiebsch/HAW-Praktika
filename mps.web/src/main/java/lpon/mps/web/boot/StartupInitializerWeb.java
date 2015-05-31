package lpon.mps.web.boot;

import lpon.mps.stammdatenadapter.entities.Artikel;
import lpon.mps.stammdatenadapter.repositories.ArtikelRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class StartupInitializerWeb implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(StartupInitializerWeb.class);

	@Autowired
	private ArtikelRepository artikelRepository;

	public void onApplicationEvent(ContextRefreshedEvent event) {
		// startup of root context, refresh will trigger for initialization and refresh of context
		if (event.getApplicationContext().getParent() == null) {
			configure();
		}
	}

	private void configure() {
		LOGGER.info("setup db.");
		Artikel schraubeArtikel = new Artikel();
		schraubeArtikel.setBezeichnung("Schraube");

		Artikel rasenmaeherArtikel = new Artikel();
		rasenmaeherArtikel.setBezeichnung("Rasenmäher");
		
		Artikel nupsiArtikel = new Artikel();
		nupsiArtikel.setBezeichnung("Nupsi");
		
		Artikel[] users = new Artikel[]{schraubeArtikel, rasenmaeherArtikel, nupsiArtikel};
		
		for(Artikel user: users)
			artikelRepository.save(user);
	}

}
