package com.walmart.routeplanner.services.map.processor;

import com.walmart.routeplanner.services.utils.BufferList;

/**
 * Utility class to load a map to database.
 * The process is done in chunks of size {@code batchSize}.
 *
 * @author Rodrigo Marchesini
 */
public class MapDatabaseLoader implements MapProcessor {
    private Integer batchSize;
    private BufferList<MapRouteParsedEvent> buffer;

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
        buffer = new BufferList<MapRouteParsedEvent>(batchSize);
    }

    @Override
    public void processRoute(MapRouteParsedEvent event) {
        buffer.add(event);
        if (buffer.isFull()) {
            flush();
        }
    }

    private void flush() {
        for (MapRouteParsedEvent e : buffer) {
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
