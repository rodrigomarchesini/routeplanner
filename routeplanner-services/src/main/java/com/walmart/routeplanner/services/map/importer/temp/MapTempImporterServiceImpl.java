package com.walmart.routeplanner.services.map.importer.temp;

import java.io.File;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${map.tmpDir}")
    private String mapFilesDir;
    
    public MapTempImporterServiceImpl() {
    }

    @Override
    public void importMap(String mapName, InputStream in) {
        File outputFile = new File(mapFilesDir, mapName + ".txt");
        MapFileWriter mapFileWriter = new MapFileWriter(outputFile);
        MapProcessor importer = new MapProcessor(mapFileWriter, new RouteParser());
        importer.importMap(mapName, in);
    }

    @PostConstruct
    public void init() {
        createDirIfNotExists();
    }
    
    private void createDirIfNotExists() {
        File dir = new File(mapFilesDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

}
