package com.walmart.routeplanner.model.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.WeightedPath;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.routeplanner.model.MapInfo;
import com.walmart.routeplanner.model.RouteInfo;
import com.walmart.routeplanner.model.ShortestPathInfo;
import com.walmart.routeplanner.model.entity.Point;
import com.walmart.routeplanner.model.entity.RelationshipTypes;
import com.walmart.routeplanner.model.repositories.PointRepository;
import com.walmart.routeplanner.model.services.MapService;

/**
 * MapService implementation
 *
 * @author Rodrigo Marchesini
 */
@Service
public class MapServiceImpl implements MapService {

    @Autowired
    private Neo4jTemplate template;

    @Autowired
    private PointRepository pointRepository;

    @Transactional
    @Override
    public void deleteMap(String mapName) {
        pointRepository.deleteAllPointsAndRoutesByMap(mapName);
    }

    @Transactional
    @Override
    public void createMap(MapInfo map) {
        Map<String, Point> nameToPointMap = createPoints(map);
        createRoutes(map, nameToPointMap);
    }

    private Map<String, Point> createPoints(MapInfo map) {
        Map<String, Point> nameToPointMap = new HashMap<String, Point>();
        final String mapName = map.getMapName();
        for (String pointName : map.getPoints()) {
            Point point = pointRepository.save(new Point(pointName, mapName));
            nameToPointMap.put(pointName, point);
        }
        return nameToPointMap;
    }

    private void createRoutes(MapInfo map, Map<String, Point> nameToPointMap) {
        for (RouteInfo route : map.getRoutes()) {
            Point origin = nameToPointMap.get(route.getFrom());
            Point destination = nameToPointMap.get(route.getTo());
            // TODO duplicated routes: is keeping that inserted first
            // instead of the one with the smallest cost
            origin.goesTo(destination, route.getCost());
            pointRepository.save(origin);
        }
    }

    @Override
    public ShortestPathInfo shortestPath(String mapName, String from, String to) {
        // TODO handle from eq to
        Point origin = pointRepository.findByNameAndMap(from, mapName);
        Point destination = pointRepository.findByNameAndMap(to, mapName);

        Node fromNode = template.getNode(origin.getId());
        Node toNode = template.getNode(destination.getId());

        WeightedPath path = GraphAlgoFactory
                .dijkstra(PathExpanders.forTypeAndDirection(RelationshipTypes.GOES_TO, Direction.OUTGOING), "cost")
                .findSinglePath(fromNode, toNode);

        ShortestPathInfo pathInfo = new ShortestPathInfo();
        if (path != null) {
            for (Relationship route : path.relationships()) {
                pathInfo.addStep(RouteInfo.of(
                        (String) route.getStartNode().getProperty("name"),
                        (String) route.getEndNode().getProperty("name"),
                        (Double) route.getProperty("cost")));
            }
            pathInfo.setTotalCost(path.weight());
        }

        return pathInfo;
    }

}
