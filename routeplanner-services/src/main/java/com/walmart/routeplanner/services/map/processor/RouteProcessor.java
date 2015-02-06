package com.walmart.routeplanner.services.map.processor;

import java.io.IOException;

import com.walmart.routeplanner.domain.model.entity.Route;

/**
 * Event handler to process a map, route by route.
 *
 * @author Rodrigo Marchesini
 */
public interface RouteProcessor {

    /**
     * Fired before reading, allowing resources preparation.
     *
     * @throws IOException
     */
    void before() throws IOException;

    /**
     * Fired after a line is correctly parsed.
     *
     * @param event Holder for line data
     * @throws IOException thrown if any error occurs
     */
    void processRoute(Route event) throws IOException;

    /**
     * Fired after reading, allowing resources to be closed.
     *
     * @throws IOException
     */
    void finished() throws IOException;
}
