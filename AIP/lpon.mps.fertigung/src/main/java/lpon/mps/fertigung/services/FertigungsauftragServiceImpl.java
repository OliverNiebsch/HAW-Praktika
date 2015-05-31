package lpon.mps.fertigung.services;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import lpon.mps.auftragsverwaltung.entities.Auftrag;
import lpon.mps.fertigung.entities.Fertigungsauftrag;
import lpon.mps.fertigung.entities.Fertigungsplan;
import lpon.mps.fertigung.repositories.FertigungsauftragRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FertigungsauftragServiceImpl implements FertigungsauftragService {

	protected EntityManager entityManager;
	
	@Autowired
	private FertigungsauftragRepository fertigungsauftragRepository;
	
	@PersistenceContext 
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
	
	@Override
	public Fertigungsauftrag getFertigungsauftrag(long id) {
		return fertigungsauftragRepository.findOne(id);
	}

	@Override
	public void save(Fertigungsauftrag t) {
		fertigungsauftragRepository.save(t);
	}

	@Override
	public Fertigungsauftrag createFertigungsauftrag(Auftrag a) {
		Fertigungsauftrag fertA = new Fertigungsauftrag(a);
		return fertigungsauftragRepository.save(fertA);
	}

	@Override
	public Fertigungsauftrag getFertigungsauftragForFertigungsplan(
			Fertigungsplan plan) {
		Query query = entityManager.createQuery("SELECT a FROM Artikel AS a WHERE a.fertigungsplan=?1");
		query.setParameter(1, plan);
		return null;
	}
}
