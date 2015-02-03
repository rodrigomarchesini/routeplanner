package com.walmart.routeplanner.services.map.processor;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.walmart.routeplanner.services.map.processor.exception.MapProcessingException;

/**
 * Test for MapImporter.
 *
 * @author Rodrigo Marchesini
 */
@RunWith(MockitoJUnitRunner.class)
public class MapImporterTest {

    @Test
    public void firesMapProcessor() throws IOException {
        String[] input = {
                "A B 10",
                "B D 15",
                "A C 20",
                "C D 30",
                "B E 50",
                "D E 30" };

        List<MapRouteParsedEvent> expectedEvents = Arrays.asList(
                new MapRouteParsedEvent("A", "B", 10),
                new MapRouteParsedEvent("B", "D", 15),
                new MapRouteParsedEvent("A", "C", 20),
                new MapRouteParsedEvent("C", "D", 30),
                new MapRouteParsedEvent("B", "E", 50),
                new MapRouteParsedEvent("D", "E", 30));

        // mock - verification of events firing
        MapProcessor processor = Mockito.mock(MapProcessor.class);

        // spy - real parse + input by MapImporter verification
        MapRouteParser routeParser = Mockito.spy(MapRouteParser.class);

        MapImporter importer = new MapImporter(processor, routeParser);
        importer.importMap("map1", toInputStream(input));

        verify(processor, times(1)).before();

        for (int i = 0; i < input.length; i++) {
            String expectedRouteString = input[i];
            MapRouteParsedEvent expectedEvent = expectedEvents.get(i);

            verify(routeParser).parseRoute(expectedRouteString, i + 1);
            verify(processor).processRoute(Matchers.eq(expectedEvent));
        }

        verify(processor, times(1)).finished();
    }

    @Test(expected = MapProcessingException.class)
    public void mapProcessorAlwaysFinishes() throws IOException {
        String input = "A B 10\nB D 15\nA C 20\nC D 30\nB E 50\nD E 30";

        MapProcessor processor = Mockito.mock(MapProcessor.class);
        doThrow(IOException.class)
                .when(processor)
                .processRoute(any(MapRouteParsedEvent.class));
        MapRouteParser routeParser = Mockito.mock(MapRouteParser.class);

        MapImporter importer = new MapImporter(processor, routeParser);
        importer.importMap("map1", toInputStream(input));

        verify(processor, times(1)).before();
        verify(processor).processRoute(Matchers.any(MapRouteParsedEvent.class));
        verify(processor, times(1)).finished();
    }

    private InputStream toInputStream(String routes) {
        return new ByteArrayInputStream(routes.getBytes());
    }

    private InputStream toInputStream(String[] routes) {
        return new ByteArrayInputStream(StringUtils.join(routes, "\n").getBytes());
    }
}
