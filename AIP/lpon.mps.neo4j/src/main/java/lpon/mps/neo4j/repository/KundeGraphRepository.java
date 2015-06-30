package lpon.mps.neo4j.repository;

import java.util.Collection;

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
		public String getProdukt() {
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
		
		public void setProdukt(String produkt) {
			this.produkt = produkt;
		}

		public void setStadt(String stadt) {
			this.stadt = stadt;
		}

		public void setAnzahl(Integer anzahl) {
			this.anzahl = anzahl;
		}

		@Override
		public String toString() {
			return "SalesData [produkt=" + getProdukt() + ", Stadt=" + getStadt()+ ", anzahl=" + getAnzahl() + "]";
		}
	}

	public KundeNode findByDbid(Long id);

//	@Query("MATCH (k:KundeNode)-[rel]->(p:ProduktNode), (k:KundeNode)-[rel2]->(p2:ProduktNode) WHERE k = k RETURN p.produktName, collect(p2.produktName) ORDER BY p.produktName;")
	@Query("MATCH (k:KundeNode)-[rel]->(p:ProduktNode), (k:KundeNode)-[rel2]->(p2:ProduktNode) WHERE k = k RETURN p.produktName, collect(p2.produktName);")
	Iterable<ProductKaufteAuchProductDataImpl> showProduktKauftenAuchProdukt();
	
	@QueryResult
	public class ProductKaufteAuchProductDataImpl implements ProductKaufteAuchProductData{

		@ResultColumn("p.produktName")
		private String produktName;
		
		@ResultColumn("collect(p2.produktName)")
		private Iterable<String> zusammenGekaufteProdukte;
		
//		@ResultColumn("collect(p2.produktName)")
//		private String referencedProducts;
		
		@Override
		public String getProduktName() {
			return produktName;
		}

		public void setZusammenGekaufteProdukte(Iterable<String> referencedProducts) {
			this.zusammenGekaufteProdukte = referencedProducts;
		}

		public void setProduktName(String produktName) {
			this.produktName = produktName;
		}

		@Override
		public Iterable<String> getZusammenGekaufteProdukte() {
			return zusammenGekaufteProdukte;
		}
		
		@Override
		public String toString() {
			return "ProductKaufteAuchProductDataImpl [produkt=" + getProduktName() + ", getAuchGekaufteProdukte=" + getZusammenGekaufteProdukte() + "]";
		}		
	}
	
}