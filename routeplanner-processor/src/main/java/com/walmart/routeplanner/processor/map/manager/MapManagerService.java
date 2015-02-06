package com.walmart.routeplanner.processor.map.manager;

import java.io.InputStream;

/**
* Service to manage maps.
* 
* @author Rodrigo Marchesini
*/
public interface MapManagerService {

    MapCreationResponse addMap(String mapName, InputStream in);
}
