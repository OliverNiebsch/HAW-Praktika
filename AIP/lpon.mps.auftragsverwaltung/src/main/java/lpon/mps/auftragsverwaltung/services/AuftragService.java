package lpon.mps.auftragsverwaltung.services;

import java.util.List;

import lpon.mps.auftragsverwaltung.entities.Angebot;
import lpon.mps.auftragsverwaltung.entities.Auftrag;
import lpon.mps.stammdatenadapter.entities.Kunde;

public interface AuftragService {
	public List<Auftrag> getAuftraegeFuerKunde(Kunde k);
	
	public Auftrag getAuftrag(long id);
	
	public Auftrag createAuftrag(Angebot a);
	
	public Auftrag save(Auftrag t);
}
