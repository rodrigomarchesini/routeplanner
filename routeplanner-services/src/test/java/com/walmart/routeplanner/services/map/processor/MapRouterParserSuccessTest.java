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

    private RouteParser routeParser;
    private String routeString;
    private RouteParsedEvent expectedParsedRoute;

    public MapRouterParserSuccessTest(String routeString, RouteParsedEvent parsedRoute) {
        this.routeString = routeString;
        expectedParsedRoute = parsedRoute;
    }

    @Before
    public void setUp() {
        routeParser = new RouteParser();
    }

    @Parameters
    public static Collection<Object[]> routes() {
        return Arrays.asList(new Object[][] {
                { "A B 10", new RouteParsedEvent("A", "B", 10) },
                { "B C 20 ", new RouteParsedEvent("B", "C", 20) }
        });
    }

    @Test
    public void testParseRoutes() {
        Assert.assertEquals(expectedParsedRoute, routeParser.parseRoute(routeString, 1));
    }

}
