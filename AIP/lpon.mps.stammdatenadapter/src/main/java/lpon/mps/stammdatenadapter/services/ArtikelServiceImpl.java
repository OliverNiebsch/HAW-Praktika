package lpon.mps.stammdatenadapter.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import lpon.mps.stammdatenadapter.entities.Artikel;
import lpon.mps.stammdatenadapter.repositories.ArtikelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArtikelServiceImpl implements ArtikelService {

	protected EntityManager entityManager;
	
	@Autowired
	protected ArtikelRepository artikelRepository;
	
    @PersistenceContext 
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
	
	@Override
	public Artikel saveArtikel(Artikel a) {
		return artikelRepository.save(a);
	}

	@Override
	public List<Artikel> getArtikel(String search) {
    	Query query;
    	if(search!=null) {
    		query = entityManager.createQuery("SELECT a FROM Artikel AS a WHERE a.bezeichnung LIKE ?1");
    		query.setParameter(1, "%"+search+"%");
    	} else {
    		query = entityManager.createQuery("SELECT a FROM Artikel AS a"); 
    	}
    	@SuppressWarnings("unchecked")
		List<Artikel> result = (List<Artikel>) query.getResultList();
		return result;
	}

	@Override
	public Artikel getArtikelById(long id) {
		return artikelRepository.findOne(id);
	}

}
