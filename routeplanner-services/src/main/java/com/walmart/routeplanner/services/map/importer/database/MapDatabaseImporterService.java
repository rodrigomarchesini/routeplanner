package com.walmart.routeplanner.services.map.importer.database;

/**
 * Service to load a map into database,
 * creation all points and routes.
 * 
 * @author Rodrigo Marchesini
 */
public interface MapDatabaseImporterService {

    /**
     * Imports a map into database.
     * If a map with this name already exists, it is
     * deleted (full replaced).
     * @param mapName
     */
    void importMapToDatabase(String mapName);
}