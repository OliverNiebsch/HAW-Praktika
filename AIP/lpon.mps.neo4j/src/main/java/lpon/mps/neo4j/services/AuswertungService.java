package lpon.mps.neo4j.services;

import lpon.mps.neo4j.dto.ProductKaufteAuchProductData;
import lpon.mps.neo4j.dto.ProductSalesData;
import lpon.mps.neo4j.nodes.KundeNode;
import lpon.mps.neo4j.nodes.ProduktNode;

public interface AuswertungService {

	Iterable<? extends ProductSalesData> showProductSalesWithCity();
	Iterable<? extends ProductKaufteAuchProductData> showProductKaufteAuchProduct();

	KundeNode getOrCreateKunde(Long id, String name, String stadt);

	ProduktNode getOrCreateProdukt(Long id, String name);

	void addBestellung(KundeNode k, ProduktNode p, int i);

}
