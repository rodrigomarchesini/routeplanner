package com.walmart.routeplanner.processor.map.processing.exception;

/**
 * Indicates bad map data that does not correspond to the
 * expected format.
 *
 * @author Rodrigo Marchesini
 */
public class MalformedMapException extends RuntimeException {

    private static final long serialVersionUID = -793149054310474816L;

    public MalformedMapException() {
    }

    public MalformedMapException(String message) {
        super(message);
    }

    public MalformedMapException(Throwable cause) {
        super(cause);
    }

    public MalformedMapException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedMapException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
