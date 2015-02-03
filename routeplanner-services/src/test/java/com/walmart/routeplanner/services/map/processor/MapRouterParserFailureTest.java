package com.walmart.routeplanner.services.map.processor;

import org.junit.Before;
import org.junit.Test;

import com.walmart.routeplanner.services.map.processor.exception.MalformedMapException;

/**
 * Failure test scenarios for MapRouterParser.
 *
 * @author Rodrigo Marchesini
 */
public class MapRouterParserFailureTest {

    private MapRouteParser routeParser;

    @Before
    public void setUp() {
        routeParser = new MapRouteParser();
    }

    @Test(expected = MalformedMapException.class)
    public void badRouteFormat() {
        String route = "A  B 10"; // two spaces
        routeParser.parseRoute(route, 1);
    }

    @Test(expected = MalformedMapException.class)
    public void negativeCostValue() {
        String route = "A B -1";
        routeParser.parseRoute(route, 1);
    }

    @Test(expected = MalformedMapException.class)
    public void invalidCostValueString() {
        String route = "A B C";
        routeParser.parseRoute(route, 1);
    }

    @Test(expected = MalformedMapException.class)
    public void invalidCostValueFloat() {
        String route = "A B 10.3";
        routeParser.parseRoute(route, 1);
    }
}
