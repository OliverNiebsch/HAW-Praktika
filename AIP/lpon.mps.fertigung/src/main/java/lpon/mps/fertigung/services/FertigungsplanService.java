package lpon.mps.fertigung.services;

import lpon.mps.fertigung.entities.Fertigungsplan;

public interface FertigungsplanService {
	public Fertigungsplan getFertigungsplan(long id);
	public Fertigungsplan save(Fertigungsplan f);
}
