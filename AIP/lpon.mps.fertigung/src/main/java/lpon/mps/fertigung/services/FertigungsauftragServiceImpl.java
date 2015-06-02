package lpon.mps.fertigung.services;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import lpon.mps.auftragsverwaltung.entities.Auftrag;
import lpon.mps.auftragsverwaltung.services.AuftragService;
import lpon.mps.fertigung.entities.Fertigungsauftrag;
import lpon.mps.fertigung.entities.Fertigungsplan;
import lpon.mps.fertigung.repositories.FertigungsauftragRepository;
import lpon.mps.fertigung.repositories.FertigungsplanRepository;
import lpon.mps.stammdatenadapter.entities.Artikel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FertigungsauftragServiceImpl implements FertigungsauftragService {

	protected EntityManager entityManager;
	
	@Autowired
	private FertigungsplanRepository planRepo;
	
	@Autowired
	private AuftragService auftragService;
	
	@Autowired
	private FertigungsplanService fertigungsplanService;
	
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
	public Fertigungsauftrag save(Fertigungsauftrag f) {
		return fertigungsauftragRepository.save(f);
	}

	@Override
	public Fertigungsauftrag createFertigungsauftrag(Auftrag a) {
		Fertigungsauftrag fertA = new Fertigungsauftrag(a);
		
		planRepo.save(fertA.getFertigungsplan());
		return fertigungsauftragRepository.save(fertA);
	}

	@Override
	public Fertigungsauftrag getFertigungsauftragForFertigungsplan(
			Fertigungsplan plan) {
		Query query = entityManager.createQuery("SELECT f FROM Fertigungsauftrag AS f WHERE f.fertigungsplan=?1");
		query.setParameter(1, plan);
		
		return (Fertigungsauftrag)query.getResultList().get(0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void bauteilGefertigt(Artikel bauteil) {
		Query query = entityManager.createQuery("SELECT f FROM Fertigungsauftrag AS f WHERE :element MEMBER OF f.auftrag.angebot.positionen");
		query.setParameter("element", bauteil);
		
		List<Fertigungsauftrag> fertAuftList = (List<Fertigungsauftrag>) query.getResultList();
		
		for (Fertigungsauftrag fertA : fertAuftList) {
			fertA.signalGefertigtesTeil(bauteil);
			
			auftragService.save(fertA.getAuftrag());
			fertigungsplanService.save(fertA.getFertigungsplan());
			save(fertA);
		}
	}
}
