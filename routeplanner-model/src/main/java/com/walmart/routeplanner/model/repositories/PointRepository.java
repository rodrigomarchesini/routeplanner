package com.walmart.routeplanner.model.repositories;

import org.springframework.data.neo4j.repository.GraphRepository;

import com.walmart.routeplanner.model.entity.Point;

/**
 * Neo4j Repository interface for Point.
 *
 * @author Rodrigo Marchesini
 */
public interface PointRepository extends GraphRepository<Point> {

}
