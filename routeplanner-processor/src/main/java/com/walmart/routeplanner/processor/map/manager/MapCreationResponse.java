package com.walmart.routeplanner.processor.map.manager;

/**
* Possible responses for a map creation request.
* 
* @author Rodrigo Marchesini
*/
public enum MapCreationResponse {
    OK_SCHEDULED,
    ERROR_INVALID_MAP_NAME,
    ERROR_BUSY
}
