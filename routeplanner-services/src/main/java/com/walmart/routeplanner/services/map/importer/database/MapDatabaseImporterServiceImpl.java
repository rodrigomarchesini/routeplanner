package com.walmart.routeplanner.services.map.importer.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.walmart.routeplanner.domain.model.entity.MapInfo;
import com.walmart.routeplanner.domain.services.MapService;
import com.walmart.routeplanner.services.map.processor.MapProcessor;
import com.walmart.routeplanner.services.map.processor.RouteParser;
import com.walmart.routeplanner.services.map.processor.exception.MissingMapFileException;

/**
 * Implementation of MapDatabaseImporterService
 * 
 * @author Rodrigo Marchesini
 */
@Service
public class MapDatabaseImporterServiceImpl implements MapDatabaseImporterService {

    @Value("${map.tmpDir}")
    private String mapFilesDir;

    @Autowired
    private MapService mapService;

    public MapDatabaseImporterServiceImpl() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.walmart.routeplanner.services.map.importer.database.
     * MapDatabaseImporterService#importMapToDatabase(java.lang.String)
     */
    @Override
    public void importMapToDatabase(String mapName) {
        mapFilesDir = "./map-files-test";

        File dir = new File(mapFilesDir);
        // TODO define pattern and ensure uniqueness
        File inputFile = new File(dir, mapName + ".txt");
        FileInputStream fin;
        try {
            fin = new FileInputStream(inputFile);
        } catch (FileNotFoundException e) {
            throw new MissingMapFileException("Map not found: " + mapName);
        }

        InMemoryRouteProcessor routeProcessor = new InMemoryRouteProcessor();
        RouteParser routeParser = new RouteParser();
        MapProcessor processor = new MapProcessor(routeProcessor, routeParser);
        processor.importMap(mapName, fin);

        MapInfo mapInfo = new MapInfo(mapName, routeProcessor.getRoutes());
        createPoints(mapInfo);
        createRoutes(mapInfo);
    }

    @Transactional
    private void deleteMap() {

    }

    @Transactional
    private void createPoints(MapInfo mapInfo) {
        mapService.createPoints(mapInfo);
    }

    private void createRoutes(MapInfo mapInfo) {
        int size = mapInfo.getRoutes().size();
        for (int i = 0; i < size; i += 5000) {
            int fromIndex = i;
            int toIndex = Math.min(i + 5000, size);
            createSomeRoutes(mapInfo, fromIndex, toIndex);
        }
    }

    @Transactional
    private void createSomeRoutes(MapInfo mapInfo, int fromIndex, int toIndex) {
        mapService.createRoutes(mapInfo, fromIndex, toIndex);
    }
}
