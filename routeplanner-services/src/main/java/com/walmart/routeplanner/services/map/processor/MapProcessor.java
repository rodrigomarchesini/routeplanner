package com.walmart.routeplanner.services.map.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.walmart.routeplanner.domain.model.entity.Point;
import com.walmart.routeplanner.domain.model.entity.Route;
import com.walmart.routeplanner.services.map.processor.exception.MapProcessingException;

/**
 * Utility class to process a map.
 * Map format is expected as follows:
 *
 * <pre>
 * origin destination cost [linebreak]
 * </pre>
 *
 * Example:<br>
 * A B 10<br>
 * A D 15<br>
 * B C 23
 *
 * @author Rodrigo Marchesini
 */
public class MapProcessor {

    private RouteProcessor routeProcessor;
    private RouteParser routeParser;

    public MapProcessor(RouteProcessor routeProcessor, RouteParser routeParser) {
        this.routeProcessor = routeProcessor;
        this.routeParser = routeParser;
    }

    /**
     * Process the map given by the InputStream.
     *
     * @param mapName Map's name
     * @param in InputStream to get map's data
     */
    public void importMap(String mapName, InputStream in) {
        try (
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(isr)) {
            routeProcessor.before();
            parseMap(mapName, reader);
        } catch (IOException e) {
            throw new MapProcessingException("Error processing map " + mapName, e);
        } finally {
            try {
                routeProcessor.finished();
            } catch (IOException e) {
                throw new MapProcessingException("Error closing routeProcessor of map " + mapName, e);
            }
        }
    }

    private void parseMap(String mapName, BufferedReader reader) throws IOException {
        String routeAsString;
        int routeCount = 0;
        while ((routeAsString = reader.readLine()) != null) {
            routeCount++;
            RouteParsedEvent info = routeParser.parseRoute(routeAsString, routeCount);
            routeProcessor.processRoute(new Route(
                    new Point(info.getOrigin(), mapName),
                    new Point(info.getDestination(), mapName),
                    info.getCost()));
        }
    }
}
