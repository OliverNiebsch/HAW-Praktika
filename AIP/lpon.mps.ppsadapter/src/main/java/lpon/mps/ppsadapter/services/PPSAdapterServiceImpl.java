package lpon.mps.ppsadapter.services;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import lpon.mps.fertigung.services.FertigungsauftragService;
import lpon.mps.stammdatenadapter.entities.Artikel;

public class PPSAdapterServiceImpl implements PPSAdapterService {
	private static ArrayList<Artikel> zuFertigendenBauteile = new ArrayList<Artikel>();
	
	@Autowired
	private FertigungsauftragService fertigungsauftragService;
	
	public void bauteilFertigen(Artikel bauteil) {
		if (!zuFertigendenBauteile.contains(bauteil))
			zuFertigendenBauteile.add(bauteil);
	}
}
