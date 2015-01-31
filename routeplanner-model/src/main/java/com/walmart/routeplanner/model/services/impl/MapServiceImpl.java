package com.walmart.routeplanner.model.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.support.Neo4jTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.routeplanner.model.RouteInfo;
import com.walmart.routeplanner.model.ShortestPathInfo;
import com.walmart.routeplanner.model.repositories.PointRepository;
import com.walmart.routeplanner.model.services.MapService;

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

    @Override
    public void createMap(String mapName, List<RouteInfo> routeInfos) {
        // TODO Auto-generated method stub
    }

    @Override
    public ShortestPathInfo shortestPath(String mapName, String from, String to) {
        // TODO Auto-generated method stub
        return null;
    }

}
