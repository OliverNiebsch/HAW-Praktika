package lpon.mps.auftragsverwaltung.services;

import lpon.mps.auftragsverwaltung.entities.Angebot;
import lpon.mps.auftragsverwaltung.entities.Auftrag;

public interface AuftragService {
	public Auftrag getAuftrag(long id);
	
	public Auftrag createAuftrag(Angebot a);
	
	public Auftrag save(Auftrag t);
}
