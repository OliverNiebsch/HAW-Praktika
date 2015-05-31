package lpon.mps.ppsadapter.services;

import java.util.ArrayList;

import lpon.mps.stammdatenadapter.entities.Artikel;

public class PPSAdapterServiceImpl implements PPSAdapterService {
	private static ArrayList<Artikel> zuFertigendenBauteile = new ArrayList<Artikel>();
	
	public void bauteilFertigen(Artikel bauteil) {
		zuFertigendenBauteile.add(bauteil);
	}
}
