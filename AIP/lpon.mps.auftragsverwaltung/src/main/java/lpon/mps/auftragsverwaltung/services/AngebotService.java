package lpon.mps.auftragsverwaltung.services;

import lpon.mps.auftragsverwaltung.entities.Angebot;

public interface AngebotService {
	public Angebot getAngebot(long id);
	
	public Angebot saveAngebot(Angebot angebot);
}
