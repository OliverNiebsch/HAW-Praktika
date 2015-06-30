package lpon.mps.spring.web.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lpon.mps.auftragsverwaltung.entities.Angebot;
import lpon.mps.auftragsverwaltung.entities.Auftrag;
import lpon.mps.auftragsverwaltung.services.AngebotService;
import lpon.mps.auftragsverwaltung.services.AuftragService;
import lpon.mps.neo4j.dto.ProductKaufteAuchProductData;
import lpon.mps.neo4j.dto.ProductSalesData;
import lpon.mps.neo4j.nodes.KundeNode;
import lpon.mps.neo4j.nodes.ProduktNode;
import lpon.mps.neo4j.services.AuswertungService;
import lpon.mps.stammdatenadapter.entities.Artikel;
import lpon.mps.stammdatenadapter.entities.Kunde;
import lpon.mps.stammdatenadapter.services.KundeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MpsRestApi {
	
	@Autowired
	private AuswertungService neo4jService;

	@Autowired
	private AngebotService angebotService;

	@Autowired
	private AuftragService auftragService;
	
	@Autowired
	private KundeService kundeService;
	
	@Autowired
	private AuswertungService auswertungsService;

	@RequestMapping(value = "/angebot", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Angebot> getAngebote() {
		ArrayList<Angebot> angebotList = new ArrayList<Angebot>();
		long id = 1;
		Angebot a = angebotService.getAngebot(id);

		while (a != null) {
			angebotList.add(a);
			a = angebotService.getAngebot(++id);
		}

		return angebotList;
	}

	@RequestMapping(value = "/angebot/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Angebot getAngebot(@PathVariable("id") Long id) {
		return angebotService.getAngebot(id);
	}

	@RequestMapping(value = "/auftrag", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public Auftrag auftragAnlegen(@RequestParam(value="angebot", required=true) Long angebotId) {
		Angebot ang = angebotService.getAngebot(angebotId);
		
		// neo4J
		Kunde k = ang.getKunde();
		KundeNode kn = neo4jService.getOrCreateKunde(k.getId(), k.getName(), k.getStadt());
		
		ProduktNode pn;
		for (Artikel a : ang.getPositionen()) {
			pn = neo4jService.getOrCreateProdukt(a.getId(), a.getBezeichnung());
			neo4jService.addBestellung(kn, pn, 1);	// TODO add Anzahl when added in Auftrag
		}
		
		return auftragService.createAuftrag(ang);
	}

	@RequestMapping(value = "/auftrag/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public Auftrag getAuftrag(@PathVariable("id") Long id) {
		return auftragService.getAuftrag(id);
	}
	
	@RequestMapping(value = "/auftrag", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Auftrag> getAuftraegeFuerKunde(@RequestParam(value="kunde", required=true) Long kundeId) {
		Kunde k = kundeService.getKunde(kundeId);
		
		return auftragService.getAuftraegeFuerKunde(k);
	}
	
	@RequestMapping(value = "/dashboard/produkt_region", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProductSalesData> getProdukteNachRegionen() {
		Iterable<? extends ProductSalesData> data = auswertungsService.showProductSalesWithCity();
		List<ProductSalesData> result = new ArrayList<ProductSalesData>();
		
		for (ProductSalesData psd : data) {
			result.add(psd);
		}
		
		return result;
	}
	
	@RequestMapping(value = "/dashboard/produkt_kombi", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<ProductKaufteAuchProductData> getProdukteZusammenGekauft() {
		Iterable<? extends ProductKaufteAuchProductData> data = auswertungsService.showProductKaufteAuchProduct();
		List<ProductKaufteAuchProductData> result = new ArrayList<ProductKaufteAuchProductData>();
		
		for (ProductKaufteAuchProductData pkp : data) {
			result.add(pkp);
		}
		
		return result;
	}
	
	private String join(Iterable<String> list) {
		String res = "";
		for (String s : list) {
			res += "," + s;
		}
		
		return res.substring(1);
	}
}