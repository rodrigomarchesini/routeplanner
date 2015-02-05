package com.walmart.routeplanner.services.map.manager;

import java.io.InputStream;

/**
* Service to manage maps.
* 
* @author Rodrigo Marchesini
*/
public interface MapManagerService {

    MapCreationResponse addMap(String mapName, InputStream in);
}
