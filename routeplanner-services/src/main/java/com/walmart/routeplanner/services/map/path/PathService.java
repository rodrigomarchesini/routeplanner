package com.walmart.routeplanner.services.map.path;

import com.walmart.routeplanner.domain.model.PathInfo;
import com.walmart.routeplanner.domain.services.PointNotFoundException;

/**
 * Service to find paths.
 * 
 * @author Rodrigo Marchesini
 */
public interface PathService {

    PathInfo shortestPath(String mapName, String origin, String destination, Double autonomy, Double fuelCost)
    throws PointNotFoundException;
}
