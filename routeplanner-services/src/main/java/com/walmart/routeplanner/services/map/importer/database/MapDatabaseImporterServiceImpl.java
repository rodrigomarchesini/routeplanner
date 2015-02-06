package com.walmart.routeplanner.services.map.importer.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
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
@Component
@Scope("prototype")
public class MapDatabaseImporterServiceImpl implements Runnable {

    private String mapFilesDir;

    private MapService mapService;

    private String mapName;

    public MapDatabaseImporterServiceImpl(MapService mapService, String mapName, String baseDir) {
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

    @Override
    public void run() {
        mapService.deleteAllRoutes(mapName);
        mapService.deleteAllPoints(mapName);
        importMapToDatabase();
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
        MapDatabaseImporterServiceImpl other = (MapDatabaseImporterServiceImpl) obj;
        if (mapName == null) {
            if (other.mapName != null)
                return false;
        } else if (!mapName.equals(other.mapName))
            return false;
        return true;
    }
}
