package lpon.mps.auftragsverwaltung.services;

import lpon.mps.auftragsverwaltung.entities.Angebot;
import lpon.mps.auftragsverwaltung.repositories.AngebotRepository;
import lpon.mps.stammdatenadapter.entities.Artikel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AngebotServiceImpl implements AngebotService {

	@Autowired
	private AngebotRepository angebotRepository;

	@Override
	public Angebot getAngebot(long id) {
		return angebotRepository.findOne(id);
	}

	@Override
	public Angebot saveAngebot(Angebot angebot) {
		return angebotRepository.save(angebot);
	}
}
