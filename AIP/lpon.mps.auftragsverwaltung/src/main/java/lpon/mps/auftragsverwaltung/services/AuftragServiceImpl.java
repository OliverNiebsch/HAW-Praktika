package lpon.mps.auftragsverwaltung.services;

import lpon.mps.auftragsverwaltung.entities.Angebot;
import lpon.mps.auftragsverwaltung.entities.Auftrag;
import lpon.mps.auftragsverwaltung.repositories.AuftragRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuftragServiceImpl implements AuftragService {

	@Autowired
	private AuftragRepository auftragRepository;
	
	@Override
	public Auftrag getAuftrag(long id) {
		return auftragRepository.findOne(id);
	}

	@Override
	public void save(Auftrag t) {
		auftragRepository.save(t);
	}

	@Override
	public Auftrag createAuftrag(Angebot a) {
		Auftrag auftrag = new Auftrag(a);
		return auftragRepository.save(auftrag);
	}
}
