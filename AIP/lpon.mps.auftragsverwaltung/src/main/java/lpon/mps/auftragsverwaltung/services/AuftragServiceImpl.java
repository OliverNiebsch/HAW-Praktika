package lpon.mps.auftragsverwaltung.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import lpon.mps.auftragsverwaltung.entities.Angebot;
import lpon.mps.auftragsverwaltung.entities.Auftrag;
import lpon.mps.auftragsverwaltung.repositories.AuftragRepository;
import lpon.mps.stammdatenadapter.entities.Kunde;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuftragServiceImpl implements AuftragService {

	@Autowired
	private AuftragRepository auftragRepository;
	
	private EntityManager entityManager;
	
	@PersistenceContext 
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
	
	@Override
	public Auftrag getAuftrag(long id) {
		return auftragRepository.findOne(id);
	}

	@Override
	public Auftrag save(Auftrag t) {
		return auftragRepository.save(t);
	}

	@Override
	public Auftrag createAuftrag(Angebot a) {
		Auftrag auftrag = new Auftrag(a);
		return auftragRepository.save(auftrag);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Auftrag> getAuftraegeFuerKunde(Kunde k) {
		Query query = entityManager.createQuery("SELECT a FROM Auftrag AS a WHERE a.angebot.kunde=?1");
		query.setParameter(1, k);
		return query.getResultList();
	}
}
