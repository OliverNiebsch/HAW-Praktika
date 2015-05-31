package lpon.mps.fertigung.services;

import lpon.mps.fertigung.entities.Fertigungsauftrag;

public interface FertigungsauftragService {
	public Fertigungsauftrag getFertigungsauftrag(long id);
	
	public void save(Fertigungsauftrag t);
}
