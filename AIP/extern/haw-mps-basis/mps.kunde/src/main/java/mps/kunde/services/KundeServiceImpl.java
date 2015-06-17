package mps.kunde.services;

import mps.kunde.entities.Kunde;
import mps.kunde.repositories.KundeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KundeServiceImpl implements KundeService {
	
	@Autowired
	private KundeRepository kundeRepository;

	@Override
	public Kunde createKunde() {
		Kunde a = new Kunde();
		kundeRepository.save(a);
		return a;
	}
}
