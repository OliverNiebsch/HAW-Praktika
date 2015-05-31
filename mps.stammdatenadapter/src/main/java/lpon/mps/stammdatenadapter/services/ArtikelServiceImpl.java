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
	public List<Artikel> getAllArtikel(String needle) {
		Query query;
    	if(needle!=null) {
    		query = entityManager.createQuery("SELECT a FROM Artikel WHERE Artikel.bezeichnung LIKE ?1");
    		query.setParameter(1, "%"+needle+"%");
    	} else {
    		query = entityManager.createQuery("SELECT a FROM Artikel"); 
    	}
    	@SuppressWarnings("unchecked")
		List<Artikel> result = (List<Artikel>) query.getResultList();
		return result;
	}

	@Override
	public void saveArtikel(Artikel a) {
		entityManager.persist(a);
	}

	@Override
	public Artikel getArtikelById(Long id) {
		return entityManager.find(Artikel.class, id);
	}

}
