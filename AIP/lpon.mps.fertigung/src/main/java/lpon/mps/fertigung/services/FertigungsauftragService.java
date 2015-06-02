package lpon.mps.fertigung.services;

import lpon.mps.auftragsverwaltung.entities.Auftrag;
import lpon.mps.fertigung.entities.Fertigungsauftrag;
import lpon.mps.fertigung.entities.Fertigungsplan;
import lpon.mps.stammdatenadapter.entities.Artikel;

public interface FertigungsauftragService {
	public Fertigungsauftrag getFertigungsauftrag(long id);
	
	public Fertigungsauftrag getFertigungsauftragForFertigungsplan(Fertigungsplan plan);
	
	public Fertigungsauftrag createFertigungsauftrag(Auftrag a);
	
	public Fertigungsauftrag save(Fertigungsauftrag t);
	
	public void bauteilGefertigt(Artikel bauteil);
}
