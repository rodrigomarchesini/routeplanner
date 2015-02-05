package com.walmart.routeplanner.services.map.manager;

/**
* Thrown to indicate that the name of the map is not valid.
* 
* @author Rodrigo Marchesini
*/
public class InvalidMapNameException extends RuntimeException {

    private static final long serialVersionUID = -1993663555623296423L;

    public InvalidMapNameException() {
    }

    public InvalidMapNameException(String message) {
        super(message);
    }

    public InvalidMapNameException(Throwable cause) {
        super(cause);
    }

    public InvalidMapNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMapNameException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
