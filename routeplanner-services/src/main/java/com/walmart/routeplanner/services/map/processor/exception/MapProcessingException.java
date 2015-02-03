package com.walmart.routeplanner.services.map.processor.exception;

/**
 * Signals an error while processing a map.
 *
 * @author Rodrigo Marchesini
 */
public class MapProcessingException extends RuntimeException {

    private static final long serialVersionUID = 2176353668030145536L;

    public MapProcessingException() {
    }

    public MapProcessingException(String message) {
        super(message);
    }

    public MapProcessingException(Throwable cause) {
        super(cause);
    }

    public MapProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public MapProcessingException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
