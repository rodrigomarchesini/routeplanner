package com.walmart.routeplanner.services.map.importer.temp;

import java.io.File;
import java.io.InputStream;

import org.springframework.stereotype.Service;

import com.walmart.routeplanner.services.map.processor.MapProcessor;
import com.walmart.routeplanner.services.map.processor.RouteParser;

/**
 * Implementation of MapTempImporterService to store maps
 * in a specific directory to wait to be imported to database.
 * 
 * @author Rodrigo Marchesini
 */
@Service
public class MapTempImporterServiceImpl implements MapTempImporterService {

    //TODO parameterize
    private static final String MAP_FILES_DIR = "./map-files/";
    
    public MapTempImporterServiceImpl() {
        this.createDirIfNotExists();
    }

    @Override
    public void importMap(String mapName, InputStream in) {
        //TODO concurrency control
        File outputFile = new File(getMapFilesDir(), mapName + ".txt");
        MapFileWriter mapFileWriter = new MapFileWriter(outputFile);
        MapProcessor importer = new MapProcessor(mapFileWriter, new RouteParser());
        importer.importMap(mapName, in);
    }

    private void createDirIfNotExists() {
        File dir = new File(getMapFilesDir());
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    protected String getMapFilesDir() {
        return MAP_FILES_DIR;
    }
}
