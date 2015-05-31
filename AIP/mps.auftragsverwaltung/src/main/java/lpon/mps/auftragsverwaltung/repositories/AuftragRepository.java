package lpon.mps.auftragsverwaltung.repositories;

import lpon.mps.auftragsverwaltung.entities.Auftrag;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * DAO for the Auftrag Entity. Spring data implements all method of the interfaces
 * automatically through proxies.
 */
public interface AuftragRepository extends
		PagingAndSortingRepository<Auftrag, Long> {
}
