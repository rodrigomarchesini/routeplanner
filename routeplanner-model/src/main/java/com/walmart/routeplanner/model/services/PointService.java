package com.walmart.routeplanner.model.services;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PathExpanders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.routeplanner.model.entity.Point;
import com.walmart.routeplanner.model.repositories.PointRepository;

/**
 *
 * @author Rodrigo Marchesini
 */
@Service
@Transactional
public class PointService {

    @Autowired
    private Neo4jTemplate template;

    @Autowired
    private PointRepository pointRepository;

    public Point createPoint(String name, String mapName) {
        return pointRepository.save(new Point(name, mapName));
    }

    public Point createRoute(Point from, Point to, Double cost) {
        from.goesTo(to, cost);
        return pointRepository.save(from);
    }

    public Iterable<Point> getAllPoints() {
        return pointRepository.findAll();
    }

    public Point findPoint(String name, String mapName) {
        // return pointRepository.findBySchemaPropertyValue("name", name);
        return pointRepository.findByNameAndMap(name, mapName);
    }

    public int countByMap(String mapName) {
        return pointRepository.countPointsByMap(mapName);
    }

    public WeightedPath shortestPath(Point from, Point to) {
        Node fromNode = template.getNode(from.getId());
        Node toNode = template.getNode(to.getId());

        return GraphAlgoFactory
                .dijkstra(PathExpanders.allTypesAndDirections(), "cost")
                .findSinglePath(fromNode, toNode);
    }
}
