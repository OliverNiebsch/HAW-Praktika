package lpon.mps.neo4j.dto;


public interface ProductKaufteAuchProductData {

	String getProduktName();
	Iterable<String> getZusammenGekaufteProdukte();	
}
