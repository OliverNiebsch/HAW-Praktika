package lpon.mps.neo4j.repository;

import mps.rating.graph.nodes.ProduktNode;

import org.springframework.data.neo4j.repository.GraphRepository;

public interface ProduktGraphRepository extends GraphRepository<ProduktNode> {

	ProduktNode findByDbid(Long id);

}