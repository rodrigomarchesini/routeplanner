package com.walmart.routeplanner.services.map.importer.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.routeplanner.domain.model.entity.MapInfo;
import com.walmart.routeplanner.domain.services.MapService;
import com.walmart.routeplanner.services.map.processor.MapProcessor;
import com.walmart.routeplanner.services.map.processor.RouteParser;
import com.walmart.routeplanner.services.map.processor.exception.MapProcessingException;
import com.walmart.routeplanner.services.map.processor.exception.MissingMapFileException;

/**
 * Implementation of MapDatabaseImporterService
 * 
 * @author Rodrigo Marchesini
 */
@Component
@Scope("prototype")
public class MapDatabaseImporter implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(MapDatabaseImporter.class);
    private static final Integer ROUTE_INSERT_BATCH_SIZE = 5000;

    private String mapFilesDir;

    private MapService mapService;

    private String mapName;

    public MapDatabaseImporter(MapService mapService, String mapName, String baseDir) {
        this.mapService = mapService;
        this.mapName = mapName;
        this.mapFilesDir = baseDir;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.walmart.routeplanner.services.map.importer.database.
     * MapDatabaseImporterService#importMapToDatabase(java.lang.String)
     */
    public void importMapToDatabase() {
        File dir = new File(mapFilesDir);
        File inputFile = new File(dir, mapName + ".txt");
        
        logger.info("Reading map from temp file to import to database map={} tempFile={}",
                mapName, inputFile.getName());

        InMemoryRouteProcessor routeProcessor = new InMemoryRouteProcessor();
        try(FileInputStream fin = new FileInputStream(inputFile)) {
            RouteParser routeParser = new RouteParser();
            MapProcessor processor = new MapProcessor(routeProcessor, routeParser);
            processor.importMap(mapName, fin);
        } catch (FileNotFoundException e) {
            throw new MissingMapFileException("Map not found: " + mapName);
        } catch (IOException e) {
            throw new MapProcessingException("Error while processing map: " + mapName, e);
        }

        logger.info("Map data loaded to memory map={}", mapName);
        
        MapInfo mapInfo = new MapInfo(mapName, routeProcessor.getRoutes());
        createPoints(mapInfo);
        createRoutes(mapInfo);
    }

    @Transactional
    private void createPoints(MapInfo mapInfo) {
        logger.info("Creating map points map={}", mapName);
        mapService.createPoints(mapInfo);
    }

    private void createRoutes(MapInfo mapInfo) {
        logger.info("Creating map routes map={} batchSize={}", mapName, ROUTE_INSERT_BATCH_SIZE);
        int size = mapInfo.getRoutes().size();
        for (int i = 0; i < size; i += ROUTE_INSERT_BATCH_SIZE) {
            int fromIndex = i;
            int toIndex = Math.min(i + ROUTE_INSERT_BATCH_SIZE, size);
            createSomeRoutes(mapInfo, fromIndex, toIndex);
        }
    }

    @Transactional
    private void createSomeRoutes(MapInfo mapInfo, int fromIndex, int toIndex) {
        logger.info("Creating map routes map={} fromIndex={} toIndex={}",
                mapName, fromIndex, toIndex);
        mapService.createRoutes(mapInfo, fromIndex, toIndex);
    }

    @Override
    public void run() {
        logger.info("Start task database importer map={}", mapName);

        logger.info("Deleting map routes map={}", mapName);
        mapService.deleteAllRoutes(mapName);
        logger.info("Deleting map points map={}", mapName);
        mapService.deleteAllPoints(mapName);

        logger.info("Importing map from temp file to database map={}", mapName);
        importMapToDatabase();
        logger.info("Finished task database importer map={}", mapName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mapName == null) ? 0 : mapName.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MapDatabaseImporter other = (MapDatabaseImporter) obj;
        if (mapName == null) {
            if (other.mapName != null)
                return false;
        } else if (!mapName.equals(other.mapName))
            return false;
        return true;
    }

    public static MapDatabaseImporter from(String mapName) {
        return new MapDatabaseImporter(null, mapName, null);
    }
}
