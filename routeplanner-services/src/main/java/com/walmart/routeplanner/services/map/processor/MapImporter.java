package com.walmart.routeplanner.services.map.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.walmart.routeplanner.services.map.processor.exception.MapProcessingException;

/**
 * Utility class to import a map.
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
public class MapImporter {

    private MapProcessor processor;
    private MapRouteParser routeParser;

    public MapImporter(MapProcessor processor, MapRouteParser routeParser) {
        this.processor = processor;
        this.routeParser = routeParser;
    }

    /**
     * Imports the map given by the InputStream.
     *
     * @param mapName Map's name
     * @param in InputStream to get map's data
     */
    public void importMap(String mapName, InputStream in) {
        try (
                InputStreamReader isr = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(isr)) {
            processor.before();
            parseMap(reader);
        } catch (IOException e) {
            throw new MapProcessingException("Error importing map " + mapName, e);
        } finally {
            try {
                processor.finished();
            } catch (IOException e) {
                throw new MapProcessingException("Error closing processor of map " + mapName, e);
            }
        }
    }

    private void parseMap(BufferedReader reader) throws IOException {
        String routeAsString;
        int routeCount = 0;
        while ((routeAsString = reader.readLine()) != null) {
            routeCount++;
            processor.processRoute(routeParser.parseRoute(routeAsString, routeCount));
        }
    }
}
