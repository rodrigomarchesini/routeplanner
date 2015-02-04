package com.walmart.routeplanner.services.map.importer.temp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import com.walmart.routeplanner.services.map.processor.RouteParsedEvent;
import com.walmart.routeplanner.services.map.processor.RouteProcessor;

/**
 * Utility class to write a map to a file,
 * route by route.
 *
 * @author Rodrigo Marchesini
 */
public class MapFileWriter implements RouteProcessor {

    private Writer writer;
    private File outputFile;

    public MapFileWriter(File outputFile) {
        this.outputFile = outputFile;
    }

    @Override
    public void before() throws IOException {
        writer = new BufferedWriter(new PrintWriter(outputFile));
    }

    @Override
    public void processRoute(RouteParsedEvent event) throws IOException {
        writer.write(event.toString() + "\n");
    }

    @Override
    public void finished() throws IOException {
        writer.close();
    }

}
