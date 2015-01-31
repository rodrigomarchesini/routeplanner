package com.walmart.routeplanner.model.services;

import java.util.List;

import com.walmart.routeplanner.model.RouteInfo;
import com.walmart.routeplanner.model.ShortestPathInfo;

/**
 * Service interface to deal with Map's managing.
 * 
 * @author Rodrigo Marchesini
 */
public interface MapService {

    //TODO createOrReplaceMap?
    
    void deleteMap(String mapName);
    
    void createMap(String mapName, List<RouteInfo> routeInfos);
    
    ShortestPathInfo shortestPath(String mapName, String from, String to);
}
