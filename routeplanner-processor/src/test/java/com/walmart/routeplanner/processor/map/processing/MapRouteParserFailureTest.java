package com.walmart.routeplanner.processor.map.processing;

import org.junit.Before;
import org.junit.Test;

import com.walmart.routeplanner.processor.map.processing.RouteParser;
import com.walmart.routeplanner.processor.map.processing.exception.MalformedMapException;

/**
 * Failure test scenarios for MapRouterParser.
 *
 * @author Rodrigo Marchesini
 */
public class MapRouteParserFailureTest {

    private RouteParser routeParser;

    @Before
    public void setUp() {
        routeParser = new RouteParser();
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
