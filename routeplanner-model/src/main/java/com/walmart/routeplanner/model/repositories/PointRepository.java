package com.walmart.routeplanner.model.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.walmart.routeplanner.model.entity.Point;

/**
 * Neo4j Repository interface for Point.
 *
 * @author Rodrigo Marchesini
 */
public interface PointRepository extends GraphRepository<Point> {

    /**
     * Delete all points and routes labeled as {@code mapName}.
     * @param mapName Map's name
     */
    @Query(value = "MATCH (p:Point {mapName: {0}}) OPTIONAL MATCH (p)-[r]-() DELETE p,r")
    void deleteAllPointsAndRoutesByMap(String mapName);

    /**
     * Retrieve the quantity of points grouped by the map.
     * @param mapName Map's name
     * @return the quantity of points labeled as {@code mapName}.
     */
    @Query(value = "MATCH (p:Point) WHERE p.mapName = {0} RETURN count(*)")
    int countPointsByMap(String mapName);
}
