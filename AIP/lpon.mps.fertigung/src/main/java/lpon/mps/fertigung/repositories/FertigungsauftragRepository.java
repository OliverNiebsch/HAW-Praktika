package lpon.mps.fertigung.repositories;

import java.util.List;

import lpon.mps.fertigung.entities.Fertigungsauftrag;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

/**
 * DAO for the Todo Entity.
 * Spring data implements all method of the interfaces automatically through proxies.
 */
public interface FertigungsauftragRepository extends 
	PagingAndSortingRepository<Fertigungsauftrag, Long> { 
	@Query(
            "Select t FROM Todo t WHERE LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))"
    )
    public List<Fertigungsauftrag> searchWithoutPaging(@Param("searchTerm") String searchTerm);
}
