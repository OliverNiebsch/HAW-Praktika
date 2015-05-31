package lpon.mps.fertigung.services;

import lpon.mps.fertigung.entities.Fertigungsplan;
import lpon.mps.fertigung.repositories.TopicRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FertigungsplanServiceImpl implements FertigungsplanService {

	@Autowired
	private TopicRepository fertigungsplanRepository;

	@Override
	public Fertigungsplan getTopic(long id) {
		return fertigungsplanRepository.findOne(id);
	}

}
