package com.walmart.routeplanner.model.services;

import com.walmart.routeplanner.model.MapInfo;
import com.walmart.routeplanner.model.ShortestPathInfo;

/**
 * Service interface to deal with Map's managing.
 * 
 * @author Rodrigo Marchesini
 */
public interface MapService {

    //TODO createOrReplaceMap?
    
    /**
     * Deletes a map, removing all points and routes.
     * @param mapName Map's name
     */
    void deleteMap(String mapName);
    
    /**
     * Creates the map represented by the {@code MapInfo}.
     * @param map Map's points and routes
     */
    void createMap(MapInfo map);
    
    ShortestPathInfo shortestPath(String mapName, String from, String to);
}
