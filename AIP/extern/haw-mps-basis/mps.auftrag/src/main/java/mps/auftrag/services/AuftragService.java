package mps.auftrag.services;

import java.util.Collection;

import mps.auftrag.entities.Auftrag;
import mps.kunde.entities.Kunde;

public interface AuftragService {

	Auftrag createAutrag();

	Collection<Auftrag> getAuftragByKunde(Kunde k);

	void save(Auftrag auftrag);

}
