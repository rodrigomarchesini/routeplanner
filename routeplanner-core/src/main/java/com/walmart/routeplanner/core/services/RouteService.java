package com.walmart.routeplanner.core.services;

import com.walmart.routeplanner.core.model.PathInfo;

/**
 * Services for map routes.
 * 
 * @author Rodrigo Marchesini
 */
public interface RouteService {
    /**
     * Looks for the shortest path between two points based on the
     * cost of the routes.
     *
     * @param mapName Map's name
     * @param from Origin
     * @param to Destination
     * @return
     */
    PathInfo shortestPath(String mapName, String from, String to)
                throws PointNotFoundException;
}
