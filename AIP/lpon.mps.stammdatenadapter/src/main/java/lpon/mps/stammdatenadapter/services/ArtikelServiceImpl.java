package lpon.mps.stammdatenadapter.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import lpon.mps.stammdatenadapter.entities.Artikel;

import org.springframework.stereotype.Service;

@Service
public class ArtikelServiceImpl implements ArtikelService {

	protected EntityManager entityManager;
	 
    @PersistenceContext 
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
	
	@Override
	public void saveArtikel(Artikel a) {
		entityManager.persist(a);
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
		return entityManager.find(Artikel.class, id);
	}

}