package lpon.mps.fertigung.repositories;

import lpon.mps.fertigung.entities.Fertigungsauftrag;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * DAO for the Todo Entity.
 * Spring data implements all method of the interfaces automatically through proxies.
 */
public interface FertigungsauftragRepository extends 
	PagingAndSortingRepository<Fertigungsauftrag, Long> { 
}
