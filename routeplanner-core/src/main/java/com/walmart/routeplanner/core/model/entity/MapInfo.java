package com.walmart.routeplanner.core.model.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Map data structure, composed by a name a list of routes.
 *
 * @author Rodrigo Marchesini
 */
public class MapInfo implements Serializable {

    private static final long serialVersionUID = -4838062515298838942L;

    private final String mapName;
    private final List<Route> routes;

    /**
     * @param mapName Map's name
     * @param routes Route list
     */
    public MapInfo(String mapName, List<Route> routes) {
        this.mapName = mapName;
        this.routes = routes;
    }

    public String getMapName() {
        return mapName;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public Set<String> buildPointsSet() {
        Set<String> set = new HashSet<String>();
        for (Route route : routes) {
            set.add(route.getOrigin().getName());
            set.add(route.getDestination().getName());
        }
        return set;
    }
}
