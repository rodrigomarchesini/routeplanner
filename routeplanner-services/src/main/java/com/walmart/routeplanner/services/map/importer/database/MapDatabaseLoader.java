package com.walmart.routeplanner.services.map.importer.database;

import com.walmart.routeplanner.services.map.processor.RouteParsedEvent;
import com.walmart.routeplanner.services.map.processor.RouteProcessor;
import com.walmart.routeplanner.services.utils.BufferList;

/**
 * Utility class to load a map to database.
 * The process is done in chunks of size {@code batchSize}.
 *
 * @author Rodrigo Marchesini
 */
public class MapDatabaseLoader implements RouteProcessor {
    private Integer batchSize;
    private BufferList<RouteParsedEvent> buffer;

    /**
     * Creates a MapDatabaseLoader
     *
     * @param batchSize Maximum amount of routes to be
     *            stored in memory before flushing to database.
     */
    public MapDatabaseLoader(int batchSize) {
        if (batchSize < 1) {
            throw new IllegalArgumentException("batchSize must be greater than 0");
        }
        this.batchSize = batchSize;
    }

    @Override
    public void before() {
        buffer = new BufferList<RouteParsedEvent>(batchSize);
    }

    @Override
    public void processRoute(RouteParsedEvent event) {
        buffer.add(event);
        if (buffer.isFull()) {
            flush();
        }
    }

    private void flush() {
        for (RouteParsedEvent e : buffer) {
            // TODO create transaction and persist
            System.out.println(e);
        }
        buffer.empty();
    }

    @Override
    public void finished() {
        flush();
        buffer = null;
    }

}
