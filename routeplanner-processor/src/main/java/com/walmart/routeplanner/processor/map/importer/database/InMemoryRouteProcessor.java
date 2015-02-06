package com.walmart.routeplanner.processor.map.importer.database;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.walmart.routeplanner.core.model.entity.Route;
import com.walmart.routeplanner.processor.map.processing.RouteProcessor;

/**
 * RouteProcessor that keeps routes in memory
 * until the process finishes.
 * 
 * @author Rodrigo Marchesini
 */
public class InMemoryRouteProcessor implements RouteProcessor, Iterable<Route> {

    private List<Route> routes;
    
    public InMemoryRouteProcessor() {
    }

    @Override
    public void before() throws IOException {
        routes = new ArrayList<Route>();
    }

    @Override
    public void processRoute(Route event) throws IOException {
        routes.add(event);
    }

    @Override
    public void finished() throws IOException {
    }

    public List<Route> getRoutes() {
        return routes;
    }
    
    @Override
    public Iterator<Route> iterator() {
        if (routes == null) {
            throw new IllegalStateException("Processor was not initialized");
        }
        return routes.iterator();
    }
}
