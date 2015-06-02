package lpon.mps.fertigung.services;

import lpon.mps.fertigung.entities.Fertigungsplan;
import lpon.mps.fertigung.repositories.FertigungsplanRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FertigungsplanServiceImpl implements FertigungsplanService {

	@Autowired
	private FertigungsplanRepository fertigungsplanRepository;

	@Override
	public Fertigungsplan getFertigungsplan(long id) {
		return fertigungsplanRepository.findOne(id);
	}

	@Override
	public Fertigungsplan save(Fertigungsplan f) {
		return fertigungsplanRepository.save(f);
	}

}
