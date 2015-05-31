package lpon.mps.auftragsverwaltung.repositories;

import lpon.mps.auftragsverwaltung.entities.Angebot;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * DAO for the Angebot Entity.
 * Spring data implements all methods of the interfaces automatically through proxies.
 */
public interface AngebotRepository extends PagingAndSortingRepository<Angebot, Long> {
}
