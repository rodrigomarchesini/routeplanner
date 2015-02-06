package com.walmart.routeplanner.services.map.path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.walmart.routeplanner.domain.model.PathInfo;
import com.walmart.routeplanner.domain.services.RouteService;

/**
 * Implementation of PathService
 * 
 * @author Rodrigo Marchesini
 */
@Service
public class PathServiceImpl implements PathService {

    @Autowired
    private RouteService routeService;
    
    public PathServiceImpl() {
    }

    @Override
    public PathInfo shortestPath(String mapName, String origin, String destination) {
        return routeService.shortestPath(mapName, origin, destination);
    }

}
