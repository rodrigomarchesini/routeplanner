package com.walmart.routeplanner.services.map.processor.exception;

/**
 * Thrown to indicate that the required file map was not found
 * or not accessible.
 *
 * @author Rodrigo Marchesini
 */
public class MissingMapFileException extends RuntimeException {

    private static final long serialVersionUID = -2176827476527722350L;

    public MissingMapFileException() {
        super();
    }

    public MissingMapFileException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public MissingMapFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingMapFileException(String message) {
        super(message);
    }

    public MissingMapFileException(Throwable cause) {
        super(cause);
    }

}
