package com.walmart.routeplanner.domain.services;

import com.walmart.routeplanner.domain.model.entity.MapInfo;

/**
 * Service interface to deal with Map's managing.
 *
 * @author Rodrigo Marchesini
 */
public interface MapService {

    /**
     * Deletes a map, removing all points and routes.
     *
     * @param mapName Map's name
     */
    void deleteMap(String mapName);

    /**
     * Insert a range of routes into database.
     * Route points must exist.
     * @param map Map data
     * @param fromIndex Begin of interval (inclusive)
     * @param toIndex End of internval (exclusive)
     */
    void createRoutes(MapInfo map, int fromIndex, int toIndex);

    /**
     * Insert all points into database.
     * Duplicated points are discarded (verified by name).
     * Point's IDs are updated after database insertion.
     * @param map Map data
     */
    void createPoints(MapInfo map);

}
