package lpon.mps.auftragsverwaltung.repositories;

import lpon.mps.auftragsverwaltung.entities.Auftrag;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface AuftragRepository extends PagingAndSortingRepository<Auftrag, Long> {
	
}
