package com.walmart.routeplanner.domain.services.impl;

import java.util.HashMap;
import java.util.Map;

import com.walmart.routeplanner.domain.model.entity.Point;

/**
 * Holds the context of map creation, keeping
 * already inserted Points in memory to avoid
 * duplication and database lookups.
 * 
 * @author Rodrigo Marchesini
 */
public class MapCreationContext {
    private Map<String, Point> points;

    public MapCreationContext() {
        points = new HashMap<String, Point>();
    }

    public MapCreationContext(int initialCapacity) {
        points = new HashMap<String, Point>(initialCapacity);
    }

    public Point get(Point point) {
        return points.get(point.getName());
    }

    public void put(Point point) {
        points.put(point.getName(), point);
    }
}