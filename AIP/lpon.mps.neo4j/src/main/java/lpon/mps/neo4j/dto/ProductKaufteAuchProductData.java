package lpon.mps.neo4j.dto;

import java.util.List;

public interface ProductKaufteAuchProductData {

	String getProduktName();
	List<String> getReferencedProducts();	
}
