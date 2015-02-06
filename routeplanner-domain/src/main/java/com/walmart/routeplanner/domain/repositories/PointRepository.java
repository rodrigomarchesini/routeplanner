package com.walmart.routeplanner.domain.repositories;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;

import com.walmart.routeplanner.domain.model.entity.Point;

/**
 * Neo4j Repository interface for Point.
 *
 * @author Rodrigo Marchesini
 */
public interface PointRepository extends GraphRepository<Point> {

    /**
     * Find a Point by its name and map.
     *
     * @param name Point's name
     * @param mapName Map's name
     * @return Point
     */
    @Query(value =
            "MATCH (p:Point) " +
            "WHERE p.name = {0} " +
            "  AND p.mapName = {1} " +
            "RETURN p")
    Point findByNameAndMap(String name, String mapName);

    @Query(value = "MATCH (p:Point {mapName: {0}}) OPTIONAL MATCH (p)-[r]-() DELETE r")
    void deleteAllRoutesByMap(String mapName);

    @Query(value = "MATCH (p:Point {mapName: {0}}) DELETE p")
    void deleteAllPointsByMap(String mapName);

    /**
     * Retrieve the quantity of points grouped by the map.
     *
     * @param mapName Map's name
     * @return the quantity of points labeled as {@code mapName}.
     */
    @Query(value = "MATCH (p:Point) WHERE p.mapName = {0} RETURN count(*)")
    int countPointsByMap(String mapName);
}
