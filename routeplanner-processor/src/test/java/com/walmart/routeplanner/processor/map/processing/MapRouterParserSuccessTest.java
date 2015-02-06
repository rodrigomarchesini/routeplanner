package com.walmart.routeplanner.processor.map.processing;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.walmart.routeplanner.core.model.entity.Point;
import com.walmart.routeplanner.core.model.entity.Route;
import com.walmart.routeplanner.processor.map.processing.RouteParser;

/**
 * Success test scenarios for MapRouterParser.
 *
 * @author Rodrigo Marchesini
 */
@RunWith(Parameterized.class)
public class MapRouterParserSuccessTest {

    private RouteParser routeParser;
    private String routeString;
    private Route expectedParsedRoute;

    public MapRouterParserSuccessTest(String routeString, Route parsedRoute) {
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
                { "A B 10", new Route(new Point("A", ""), new Point("B", ""), 10) },
                { "B C 20 ", new Route(new Point("B", ""), new Point("C", ""), 20) }
        });
    }

    @Test
    public void testParseRoutes() {
        Assert.assertEquals(expectedParsedRoute.toStringLine(), routeParser.parseRoute(routeString, 1).toStringLine());
    }

}
