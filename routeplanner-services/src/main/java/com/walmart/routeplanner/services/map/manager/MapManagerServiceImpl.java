package com.walmart.routeplanner.services.map.manager;

import java.io.File;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.walmart.routeplanner.domain.services.MapService;
import com.walmart.routeplanner.services.map.importer.database.MapDatabaseImporterServiceImpl;
import com.walmart.routeplanner.services.map.importer.temp.MapFileWriter;
import com.walmart.routeplanner.services.map.processor.MapProcessor;
import com.walmart.routeplanner.services.map.processor.RouteParser;
import com.walmart.routeplanner.services.map.processor.RouteProcessor;

/**
 * Implementation of MapManagerService to add maps
 * to the application.
 * 
 * @author Rodrigo Marchesini
 */
@Service
public class MapManagerServiceImpl implements MapManagerService {

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

        if (!checkMapLock(mapName)) {
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

    private boolean checkMapLock(String mapName) {
        return !executor.getThreadPoolExecutor().getQueue().contains(mapName);
    }

    private void saveToTempFile(String mapName, InputStream in) {
        File dir = createDirectoryIfNecessary();
        File outputFile = new File(dir, mapName + ".txt");

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
        executor.execute(new MapDatabaseImporterServiceImpl(mapService, mapName, mapFilesDir));
    }
}
