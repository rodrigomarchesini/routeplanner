package com.walmart.routeplanner.domain.model.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.BeforeTransaction;

import com.walmart.routeplanner.domain.model.entity.MapInfo;
import com.walmart.routeplanner.domain.model.entity.Point;
import com.walmart.routeplanner.domain.model.entity.Route;
import com.walmart.routeplanner.domain.repositories.PointRepository;
import com.walmart.routeplanner.domain.services.MapService;

/**
 * Base class for Map tests
 *
 * @author Rodrigo Marchesini
 */
public class BaseMapTest {

    @Autowired
    protected PointRepository pointRepository;

    @Autowired
    protected Neo4jTemplate template;

    @Autowired
    protected MapService mapService;

    public BaseMapTest() {
        super();
    }

    @Rollback(false)
    @BeforeTransaction
    public void cleanUpGraph() {
        Neo4jHelper.cleanDb(template);
    }

    protected void createMap(MapInfo map) {
        mapService.createPoints(map);
        mapService.createRoutes(map, 0, map.getRoutes().size());
    }
    
    protected MapInfo createSimpleMapInfo(String mapName) {
        List<Route> routes = new ArrayList<Route>();
        routes.add(r(mapName, "A", "B", 10));
        routes.add(r(mapName, "B", "D", 15));
        routes.add(r(mapName, "A", "C", 20));
        routes.add(r(mapName, "C", "D", 30));
        routes.add(r(mapName, "B", "E", 50));
        routes.add(r(mapName, "D", "E", 30));
        MapInfo map = new MapInfo(mapName, routes);
        return map;
    }

    protected MapInfo createDuplicatedRouteMapInfo(String mapName) {
        List<Route> routes = new ArrayList<Route>();
        routes.add(r(mapName, "A", "B", 10));
        routes.add(r(mapName, "B", "D", 15));
        routes.add(r(mapName, "A", "C", 20));
        routes.add(r(mapName, "C", "D", 30));
        routes.add(r(mapName, "B", "E", 50));
        routes.add(r(mapName, "D", "E", 30));
        // duplications
        routes.add(r(mapName, "A", "B", 2));
        routes.add(r(mapName, "B", "E", 50));
        MapInfo map = new MapInfo(mapName, routes);
        return map;
    }

    protected MapInfo createDisconnectedMapInfo(String mapName) {
        List<Route> routes = new ArrayList<Route>();
        routes.add(r(mapName, "A", "C", 1));
        routes.add(r(mapName, "B", "C", 1));
        routes.add(r(mapName, "D", "E", 2));
        routes.add(r(mapName, "D", "F", 1));
        MapInfo map = new MapInfo(mapName, routes);
        return map;
    }

    protected Route r(String map, String origin, String destination, Integer cost) {
        return new Route(p(origin, map), p(destination, map), cost);
    }

    protected Point p(String name, String map) {
        return new Point(name, map);
    }
}