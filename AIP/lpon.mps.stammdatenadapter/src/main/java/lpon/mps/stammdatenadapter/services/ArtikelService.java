package lpon.mps.stammdatenadapter.services;

import java.util.List;

import lpon.mps.stammdatenadapter.entities.Artikel;

public interface ArtikelService {
	public List<Artikel> getUsers(String needle);

	void saveUser(Artikel u);

	public Artikel getUserById(long id);
}
