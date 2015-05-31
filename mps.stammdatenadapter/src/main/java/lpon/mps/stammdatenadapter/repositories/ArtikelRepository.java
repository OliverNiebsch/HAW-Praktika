package lpon.mps.stammdatenadapter.repositories;

import lpon.mps.stammdatenadapter.entities.Artikel;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArtikelRepository extends PagingAndSortingRepository<Artikel, Long> {

	Artikel findByBezeichnung(String bezeichnung);

}
