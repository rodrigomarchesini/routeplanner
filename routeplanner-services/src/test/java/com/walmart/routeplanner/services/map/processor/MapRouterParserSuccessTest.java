package com.walmart.routeplanner.services.map.processor;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/**
 * Success test scenarios for MapRouterParser.
 *
 * @author Rodrigo Marchesini
 */
@RunWith(Parameterized.class)
public class MapRouterParserSuccessTest {

    private MapRouteParser routeParser;
    private String routeString;
    private MapRouteParsedEvent expectedParsedRoute;

    public MapRouterParserSuccessTest(String routeString, MapRouteParsedEvent parsedRoute) {
        this.routeString = routeString;
        expectedParsedRoute = parsedRoute;
    }

    @Before
    public void setUp() {
        routeParser = new MapRouteParser();
    }

    @Parameters
    public static Collection<Object[]> routes() {
        return Arrays.asList(new Object[][] {
                { "A B 10", new MapRouteParsedEvent("A", "B", 10) },
                { "B C 20 ", new MapRouteParsedEvent("B", "C", 20) }
        });
    }

    @Test
    public void testParseRoutes() {
        Assert.assertEquals(expectedParsedRoute, routeParser.parseRoute(routeString, 1));
    }

}
