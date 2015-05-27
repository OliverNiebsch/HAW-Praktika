package lpon.mps.artikel.persistence;

import org.springframework.data.repository.PagingAndSortingRepository;
import lpon.mps.artikel.persistence.Artikel;

public interface ArtikelRepository extends PagingAndSortingRepository<Artikel, Long> {
	
}
