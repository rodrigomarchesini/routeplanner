package com.walmart.routeplanner.services.map.processor;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.walmart.routeplanner.domain.model.entity.Route;
import com.walmart.routeplanner.services.map.processor.exception.MapProcessingException;

/**
 * Test for MapProcessor.
 *
 * @author Rodrigo Marchesini
 */
@RunWith(MockitoJUnitRunner.class)
public class MapProcessorTest extends BaseMapProcessingTest {

    @Test
    public void firesMapProcessor() throws IOException {
        String mapName = "map1";

        String[] input = {
                "A B 10",
                "B D 15",
                "A C 20",
                "C D 30",
                "B E 50",
                "D E 30" };

        List<Route> expectedEvents = Arrays.asList(
                r(mapName, "A", "B", 10),
                r(mapName, "B", "D", 15),
                r(mapName,"A", "C", 20),
                r(mapName,"C", "D", 30),
                r(mapName,"B", "E", 50),
                r(mapName,"D", "E", 30));

        // mock - verification of events firing
        RouteProcessor routeProcessor = Mockito.mock(RouteProcessor.class);

        // spy - real parse + input by MapProcessor verification
        RouteParser routeParser = Mockito.spy(RouteParser.class);

        MapProcessor mapProcessor = new MapProcessor(routeProcessor, routeParser);
        mapProcessor.importMap(mapName, toInputStream(input));

        verify(routeProcessor, times(1)).before();

        for (int i = 0; i < input.length; i++) {
            String expectedRouteString = input[i];
            Route expectedEvent = expectedEvents.get(i);

            verify(routeParser, atMost(input.length)).parseRoute(expectedRouteString, i + 1);
            verify(routeProcessor, atMost(input.length)).processRoute(Matchers.eq(expectedEvent));
        }

        verify(routeProcessor, times(1)).finished();
    }

    @Test(expected = MapProcessingException.class)
    public void mapProcessorAlwaysFinishes() throws IOException {
        String input = "A B 10\nB D 15\nA C 20\nC D 30\nB E 50\nD E 30";

        RouteProcessor routeProcessor = Mockito.mock(RouteProcessor.class);
        doThrow(IOException.class)
                .when(routeProcessor)
                .processRoute(any(Route.class));
        RouteParser routeParser = Mockito.spy(RouteParser.class);

        MapProcessor mapProcessor = new MapProcessor(routeProcessor, routeParser);
        mapProcessor.importMap("map1", toInputStream(input));

        verify(routeProcessor, times(1)).before();
        verify(routeProcessor).processRoute(Matchers.any(Route.class));
        verify(routeProcessor, times(1)).finished();
    }
}
