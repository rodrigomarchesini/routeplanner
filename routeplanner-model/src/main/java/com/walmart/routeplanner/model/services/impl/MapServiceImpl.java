package com.walmart.routeplanner.model.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.routeplanner.model.MapInfo;
import com.walmart.routeplanner.model.RouteInfo;
import com.walmart.routeplanner.model.ShortestPathInfo;
import com.walmart.routeplanner.model.entity.Point;
import com.walmart.routeplanner.model.repositories.PointRepository;
import com.walmart.routeplanner.model.services.MapService;

/**
 * MapService implementation
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
            Point point = this.pointRepository.save(new Point(pointName, mapName));
            nameToPointMap.put(pointName, point);
        }
        return nameToPointMap;
    }

    private void createRoutes(MapInfo map, Map<String, Point> nameToPointMap) {
        for (RouteInfo route : map.getRoutes()) {
            Point source = nameToPointMap.get(route.getFrom());
            Point destination = nameToPointMap.get(route.getTo());
            // TODO duplicated routes: is keeping that inserted first
            // instead of the one with the smallest cost
            source.goesTo(destination, route.getCost());
            this.pointRepository.save(source);
        }
    }

    @Override
    public ShortestPathInfo shortestPath(String mapName, String from, String to) {
        // TODO Auto-generated method stub
        return null;
    }

}
