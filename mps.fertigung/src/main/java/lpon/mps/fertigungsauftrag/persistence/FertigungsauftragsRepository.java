package lpon.mps.fertigungsauftrag.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface FertigungsauftragsRepository extends
		PagingAndSortingRepository<Fertigungsauftrag, Long> {

}
