package com.walmart.routeplanner.services.map.importer.temp;

import java.io.InputStream;

/**
 * Service to save map data do a file.
 * Map data is read through an {@link InputStream}, parsed
 * and written to a file. 
 * 
 * @author Rodrigo Marchesini
 */
public interface MapTempImporterService {

    /**
     * Imports a map
     * @param mapName Map's name
     * @param in InputStream to get map's routes
     */
    void importMap(String mapName, InputStream in);
}
