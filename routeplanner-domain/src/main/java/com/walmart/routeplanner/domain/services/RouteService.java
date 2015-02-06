package com.walmart.routeplanner.domain.services;

import com.walmart.routeplanner.domain.model.PathInfo;

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
    PathInfo shortestPath(String mapName, String from, String to);
}
