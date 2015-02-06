package com.walmart.routeplanner.services.map.path;

import com.walmart.routeplanner.domain.model.PathInfo;

/**
 * Service to find paths.
 * 
 * @author Rodrigo Marchesini
 */
public interface PathService {

    PathInfo shortestPath(String mapName, String origin, String destination);
}
