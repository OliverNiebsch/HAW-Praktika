package lpon.mps.fertigung.repositories;

import lpon.mps.fertigung.entities.Fertigungsplan;

import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * DAO for the Todo Entity.
 * Spring data implements all methods of the interfaces automatically through proxies.
 */
public interface FertigungsplanRepository extends PagingAndSortingRepository<Fertigungsplan, Long> {
}
