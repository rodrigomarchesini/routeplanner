package com.walmart.routeplanner.services.map.manager;

/**
* Possible responses for a map creation request.
* 
* @author Rodrigo Marchesini
*/
public enum MapCreationResponse {
    OK_SCHEDULED,
    ERROR_INVALID_MAP_NAME, //TODO exception?
    ERROR_BUSY
}
