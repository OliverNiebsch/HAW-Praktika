package lpon.mps.neo4j.repository;

import lpon.mps.neo4j.nodes.AuftragsPositionRelation;

import org.springframework.data.neo4j.repository.GraphRepository;

public interface AuftragsPositionGraphRepository extends GraphRepository<AuftragsPositionRelation> {

//    // TODO: Replace with @Param("name") when Spring Data Neo4j supports names vs. positional arguments
//    List<AuftragsPositionNode> findByLastName(@Param("0") String name);

}