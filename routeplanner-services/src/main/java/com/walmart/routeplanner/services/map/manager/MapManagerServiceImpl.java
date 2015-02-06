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
import com.walmart.routeplanner.services.map.manager.exception.InvalidMapNameException;
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
        validateName(mapName);
        checkMapLock(mapName);
        saveToTempFile(mapName, in);
        scheduleDatabaseCreation(mapName);
        return MapCreationResponse.OK_SCHEDULED;
    }

    private void validateName(String mapName) {
        // TODO define and create Pattern
        if (mapName == null
                || mapName.isEmpty()
                || mapName.length() > 20) {
            throw new InvalidMapNameException();
        }
    }

    private void checkMapLock(String mapName) {
        // TODO avoid update while creating
    }

    private void saveToTempFile(String mapName, InputStream in) {
        File dir = createDirectoryIfNecessary();
        // TODO define pattern and ensure uniqueness
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
