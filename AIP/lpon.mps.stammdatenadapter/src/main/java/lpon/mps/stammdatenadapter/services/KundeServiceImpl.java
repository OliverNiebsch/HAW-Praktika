package lpon.mps.stammdatenadapter.services;

import lpon.mps.stammdatenadapter.entities.Kunde;
import lpon.mps.stammdatenadapter.repositories.KundeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KundeServiceImpl implements KundeService {

	@Autowired
	private KundeRepository kundeRepository;

	@Override
	public Kunde getTopic(long id) {
		return kundeRepository.findOne(id);
	}

}
