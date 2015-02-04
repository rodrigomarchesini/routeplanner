package com.walmart.routeplanner.services.map.processor;

import static org.mockito.Matchers.any;
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
        String[] input = {
                "A B 10",
                "B D 15",
                "A C 20",
                "C D 30",
                "B E 50",
                "D E 30" };

        List<RouteParsedEvent> expectedEvents = Arrays.asList(
                new RouteParsedEvent("A", "B", 10),
                new RouteParsedEvent("B", "D", 15),
                new RouteParsedEvent("A", "C", 20),
                new RouteParsedEvent("C", "D", 30),
                new RouteParsedEvent("B", "E", 50),
                new RouteParsedEvent("D", "E", 30));

        // mock - verification of events firing
        RouteProcessor routeProcessor = Mockito.mock(RouteProcessor.class);

        // spy - real parse + input by MapProcessor verification
        RouteParser routeParser = Mockito.spy(RouteParser.class);

        MapProcessor mapProcessor = new MapProcessor(routeProcessor, routeParser);
        mapProcessor.importMap("map1", toInputStream(input));

        verify(routeProcessor, times(1)).before();

        for (int i = 0; i < input.length; i++) {
            String expectedRouteString = input[i];
            RouteParsedEvent expectedEvent = expectedEvents.get(i);

            verify(routeParser).parseRoute(expectedRouteString, i + 1);
            verify(routeProcessor).processRoute(Matchers.eq(expectedEvent));
        }

        verify(routeProcessor, times(1)).finished();
    }

    @Test(expected = MapProcessingException.class)
    public void mapProcessorAlwaysFinishes() throws IOException {
        String input = "A B 10\nB D 15\nA C 20\nC D 30\nB E 50\nD E 30";

        RouteProcessor routeProcessor = Mockito.mock(RouteProcessor.class);
        doThrow(IOException.class)
                .when(routeProcessor)
                .processRoute(any(RouteParsedEvent.class));
        RouteParser routeParser = Mockito.mock(RouteParser.class);

        MapProcessor mapProcessor = new MapProcessor(routeProcessor, routeParser);
        mapProcessor.importMap("map1", toInputStream(input));

        verify(routeProcessor, times(1)).before();
        verify(routeProcessor).processRoute(Matchers.any(RouteParsedEvent.class));
        verify(routeProcessor, times(1)).finished();
    }
}
