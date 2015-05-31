package lpon.mps.stammdatenadapter.services;

import java.util.List;

import lpon.mps.stammdatenadapter.entities.Artikel;

public interface ArtikelService {
	public List<Artikel> getArtikel(String needle);

	public Artikel saveArtikel(Artikel a);

	public Artikel getArtikelById(long id);
}
