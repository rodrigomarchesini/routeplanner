package com.walmart.routeplanner.domain.model.services.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.data.neo4j.support.node.Neo4jHelper;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.BeforeTransaction;

import com.walmart.routeplanner.domain.model.MapInfo;
import com.walmart.routeplanner.domain.model.RouteInfo;
import com.walmart.routeplanner.domain.repositories.PointRepository;
import com.walmart.routeplanner.domain.services.MapService;

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

    protected MapInfo createSimpleMapInfo(String mapName) {
        List<RouteInfo> routes = new ArrayList<RouteInfo>();
        routes.add(RouteInfo.of("A", "B", 10d));
        routes.add(RouteInfo.of("B", "D", 15d));
        routes.add(RouteInfo.of("A", "C", 20d));
        routes.add(RouteInfo.of("C", "D", 30d));
        routes.add(RouteInfo.of("B", "E", 50d));
        routes.add(RouteInfo.of("D", "E", 30d));
        MapInfo map = new MapInfo(mapName, routes);
        return map;
    }

    protected MapInfo createDuplicatedRouteMapInfo(String mapName) {
        List<RouteInfo> routes = new ArrayList<RouteInfo>();
        routes.add(RouteInfo.of("A", "B", 10d));
        routes.add(RouteInfo.of("B", "D", 15d));
        routes.add(RouteInfo.of("A", "C", 20d));
        routes.add(RouteInfo.of("C", "D", 30d));
        routes.add(RouteInfo.of("B", "E", 50d));
        routes.add(RouteInfo.of("D", "E", 30d));
        // duplications
        routes.add(RouteInfo.of("A", "B", 2d));
        routes.add(RouteInfo.of("B", "E", 50d));
        MapInfo map = new MapInfo(mapName, routes);
        return map;
    }

    protected MapInfo createDisconnectedMapInfo(String mapName) {
        List<RouteInfo> routes = new ArrayList<RouteInfo>();
        routes.add(RouteInfo.of("A", "C", 1d));
        routes.add(RouteInfo.of("B", "C", 1d));
        routes.add(RouteInfo.of("D", "E", 2d));
        routes.add(RouteInfo.of("D", "F", 1d));
        MapInfo map = new MapInfo(mapName, routes);
        return map;
    }

}