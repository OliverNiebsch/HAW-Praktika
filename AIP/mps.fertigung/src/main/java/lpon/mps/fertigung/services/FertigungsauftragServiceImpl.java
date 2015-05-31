package lpon.mps.fertigung.services;

import lpon.mps.fertigung.entities.Fertigungsauftrag;
import lpon.mps.fertigung.repositories.TodoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FertigungsauftragServiceImpl implements FertigungsauftragService {

	@Autowired
	private TodoRepository fertigungsauftragRepository;
	
	@Override
	public Fertigungsauftrag getFertigungsauftrag(long id) {
		return fertigungsauftragRepository.findOne(id);
	}

	@Override
	public void save(Fertigungsauftrag t) {
		fertigungsauftragRepository.save(t);
	}
}
