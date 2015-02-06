package com.walmart.routeplanner.processor.map.manager;

import java.io.File;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.walmart.routeplanner.core.services.MapService;
import com.walmart.routeplanner.processor.map.importer.database.MapDatabaseImporter;
import com.walmart.routeplanner.processor.map.importer.temp.MapFileWriter;
import com.walmart.routeplanner.processor.map.processing.MapProcessor;
import com.walmart.routeplanner.processor.map.processing.RouteParser;
import com.walmart.routeplanner.processor.map.processing.RouteProcessor;

/**
 * Implementation of MapManagerService to add maps
 * to the application.
 * 
 * @author Rodrigo Marchesini
 */
@Service
public class MapManagerServiceImpl implements MapManagerService {

    private static final Logger logger = LoggerFactory.getLogger(MapManagerServiceImpl.class);
    private static final int MAP_NAME_MAXLENGTH = 40;

    @Value("${map.tmpDir}")
    private String mapFilesDir;

    @Autowired
    private ThreadPoolTaskExecutor executor;

    @Autowired
    private MapService mapService;

    public MapManagerServiceImpl() {
    }

    @Override
    public MapCreationResponse addMap(String mapName, InputStream in) {
        if (!validateName(mapName)) {
            return MapCreationResponse.ERROR_INVALID_MAP_NAME;
        }

        if (!checkMapAlreadyQueued(mapName)) {
            logger.info("Map is already in queue map={}", mapName);
            return MapCreationResponse.ERROR_BUSY;
        }

        saveToTempFile(mapName, in);
        scheduleDatabaseCreation(mapName);
        return MapCreationResponse.OK_SCHEDULED;
    }

    private boolean validateName(String mapName) {
        return (mapName != null
                && !mapName.isEmpty()
                && mapName.length() < MAP_NAME_MAXLENGTH);
    }

    private boolean checkMapAlreadyQueued(String mapName) {
        return !executor.getThreadPoolExecutor().getQueue().contains(MapDatabaseImporter.from(mapName));
    }

    private void saveToTempFile(String mapName, InputStream in) {
        File dir = createDirectoryIfNecessary();
        File outputFile = new File(dir, mapName + ".txt");

        logger.info("Saving map to temp file map={} tempFile={}", mapName, outputFile.getName());
        RouteProcessor routeProcessor = new MapFileWriter(outputFile);
        RouteParser routeParser = new RouteParser();
        MapProcessor processor = new MapProcessor(routeProcessor, routeParser);
        processor.importMap(mapName, in);
    }

    private File createDirectoryIfNecessary() {
        File dir = new File(mapFilesDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    private void scheduleDatabaseCreation(String mapName) {
        logger.info("Scheduling map importing process map={}", mapName);
        executor.execute(new MapDatabaseImporter(mapService, mapName, mapFilesDir));
    }
}
