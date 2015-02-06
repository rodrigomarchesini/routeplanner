package com.walmart.routeplanner.processor.map.path;

import com.walmart.routeplanner.core.model.PathInfo;
import com.walmart.routeplanner.core.services.PointNotFoundException;

/**
 * Service to find paths.
 * 
 * @author Rodrigo Marchesini
 */
public interface PathService {

    PathInfo shortestPath(String mapName, String origin, String destination, Double autonomy, Double fuelCost)
    throws PointNotFoundException;
}
