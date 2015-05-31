package lpon.mps.auftragsverwaltung.services;

import lpon.mps.auftragsverwaltung.entities.Auftrag;

public interface AuftragService {
	public Auftrag getAuftrag(long id);
	
	public void save(Auftrag t);
}
