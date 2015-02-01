package com.walmart.routeplanner.model;

import java.io.Serializable;
import java.util.Collections;
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
    private final List<RouteInfo> routes;

    private final Set<String> points;

    /**
     * @param mapName Map's name
     * @param routes Route list
     */
    public MapInfo(String mapName, List<RouteInfo> routes) {
        this.mapName = mapName;
        this.routes = routes;

        points = buildPointsSet(routes);
    }

    private Set<String> buildPointsSet(List<RouteInfo> routes) {
        Set<String> set = new HashSet<String>();
        for (RouteInfo route : routes) {
            set.add(route.getFrom());
            set.add(route.getTo());
        }
        return Collections.unmodifiableSet(set);
    }

    public String getMapName() {
        return mapName;
    }

    public List<RouteInfo> getRoutes() {
        return routes;
    }

    public Set<String> getPoints() {
        return points;
    }
}
