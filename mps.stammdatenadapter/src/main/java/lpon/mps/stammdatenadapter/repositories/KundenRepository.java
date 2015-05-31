package lpon.mps.stammdatenadapter.repositories;

import lpon.mps.stammdatenadapter.entities.Kunde;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface KundenRepository extends PagingAndSortingRepository<Kunde, Long> {

}
