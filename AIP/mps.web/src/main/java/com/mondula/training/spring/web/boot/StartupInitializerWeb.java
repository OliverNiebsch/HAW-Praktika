package com.mondula.training.spring.web.boot;

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
public class StartupInitializerWeb implements ApplicationListener<ContextRefreshedEvent> {
	private static final Logger LOGGER = LoggerFactory.getLogger(StartupInitializerWeb.class);

	@Autowired
	private ArtikelRepository artikelRepository;

	@Autowired
	private KundeRepository kundeRepository;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// startup of root context, refresh will trigger for initialization and refresh of context
		if (event.getApplicationContext().getParent() == null) {
			configure();
		}
	}

	private void configure() {
		LOGGER.info("setup db.");
		Artikel artikel1 = new Artikel();
		artikel1.setBezeichnung("Schraube");

		Artikel artikel2 = new Artikel();
		artikel2.setBezeichnung("Nupsi");
		
		Artikel artikel3 = new Artikel();
		artikel3.setBezeichnung("Rasenmäher");
		
		Artikel[] users = new Artikel[]{artikel1, artikel2, artikel3};
		
		Kunde topicDevelopment = new Kunde();
		kundeRepository.save(topicDevelopment);
		
		Kunde topicTest = new Kunde();
		kundeRepository.save(topicTest);
		
		for(Artikel user: users)
			artikelRepository.save(user);
	}
}
