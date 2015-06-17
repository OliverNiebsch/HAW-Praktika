package lpon.mps.neo4j.repository;

import java.util.Collection;
import java.util.List;

import lpon.mps.neo4j.dto.ProductKaufteAuchProductData;
import lpon.mps.neo4j.dto.ProductSalesData;
import lpon.mps.neo4j.nodes.KundeNode;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;

public interface KundeGraphRepository extends GraphRepository<KundeNode> {

	// TODO: Replace with @Param("stadt") when Spring Data Neo4j supports names vs. positional arguments
	Collection<KundeNode> findByStadt(@Param("0") String stadt);
	
	@Query("MATCH (k:KundeNode)-[rel]->(p:ProduktNode) WHERE k = k RETURN p.produktName, k.stadt, sum(rel.anzahl) ORDER BY p.produktName, k.stadt;")
	Iterable<ProductSalesDataImpl> showProductSalesWithCity();
	
	@QueryResult
	public class ProductSalesDataImpl implements ProductSalesData{
		@ResultColumn("p.produktName")
		private String produkt;

		@ResultColumn("k.stadt")
		private String stadt;
		
		@ResultColumn("sum(rel.anzahl)")
		private Integer anzahl;
		
		@Override
		public String getProduktName() {
			return produkt;
		}

		@Override
		public String getStadt() {
			return stadt;
		}

		@Override
		public Integer getAnzahl() {
			return anzahl;
		}
		
		@Override
		public String toString() {
			return "SalesData [produkt=" + getProduktName() + ", Stadt=" + getStadt()+ ", anzahl=" + getAnzahl() + "]";
		}
	}

	public KundeNode findByDbid(Long id);

	@Query("MATCH (k:KundeNode)-[rel]->(p:ProduktNode), (k:KundeNode)-[rel2]->(p2:ProduktNode) WHERE k = k RETURN p.produktName, collect(p2.produktName) ORDER BY p.produktName;")
	Iterable<ProductKaufteAuchProductData> showProduktKauftenAuchProdukt();
	
	@QueryResult
	public class ProductKaufteAuchProductDataImpl implements ProductKaufteAuchProductData{

		@ResultColumn("p.produktName")
		private String produktName;
		
		@ResultColumn("collect(p2.produktName)")
		private List<String> referencedProducts;
		
		@Override
		public String getProduktName() {
			return produktName;
		}

		@Override
		public List<String> getAuchGekaufteProdukte() {
			return referencedProducts;
		}
		
		@Override
		public String toString() {
			return "ProductKaufteAuchProductDataImpl [produkt=" + getProduktName() + ", getAuchGekaufteProdukte=" + getAuchGekaufteProdukte() + "]";
		}		
	}
	
}