package com.walmart.routeplanner.services.map.processor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Utility class to write a map to a file,
 * route by route.
 *
 * @author Rodrigo Marchesini
 */
public class MapFileWriter implements MapProcessor {

    private Writer writer;
    private File outputFile;

    public MapFileWriter(File outputFile) {
        this.outputFile = outputFile;
    }

    @Override
    public void before() throws IOException {
        writer = new BufferedWriter(new FileWriter(outputFile));
    }

    @Override
    public void processRoute(MapRouteParsedEvent event) throws IOException {
        writer.write(event.toString());
    }

    @Override
    public void finished() throws IOException {
        writer.close();
    }

}
