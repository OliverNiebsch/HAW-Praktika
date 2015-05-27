package lpon.mps.angebot.persistence;

import java.util.List;

import lpon.mps.artikel.persistence.Artikel;
import lpon.mps.kunde.persistence.KundeDTO;

public class AngebotDTO {
	private int id;
	private KundeDTO kunde;
	private List<Artikel> positionen;
}
