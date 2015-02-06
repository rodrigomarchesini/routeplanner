package com.walmart.routeplanner.services.map.importer.temp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

import com.walmart.routeplanner.domain.model.entity.Route;
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
    public void processRoute(Route event) throws IOException {
        writer.write(event.toStringLine() + "\n");
    }

    @Override
    public void finished() throws IOException {
        writer.close();
    }

}
