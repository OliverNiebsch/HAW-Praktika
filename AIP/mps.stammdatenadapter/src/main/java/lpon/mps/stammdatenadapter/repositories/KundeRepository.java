package lpon.mps.stammdatenadapter.repositories;

import lpon.mps.stammdatenadapter.entities.Kunde;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * DAO for the Todo Entity.
 * Spring data implements all methods of the interfaces automatically through proxies.
 */
public interface KundeRepository extends PagingAndSortingRepository<Kunde, Long> {
}
