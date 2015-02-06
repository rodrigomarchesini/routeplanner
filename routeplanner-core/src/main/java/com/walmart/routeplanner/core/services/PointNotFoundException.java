package com.walmart.routeplanner.core.services;

/**
 * Thrown when a point is not found in database.
 * 
 * @author Rodrigo Marchesini
 */
public class PointNotFoundException extends Exception {

    private static final long serialVersionUID = -5185482658560812501L;

    public PointNotFoundException() {
    }

    public PointNotFoundException(String message) {
        super(message);
    }

    public PointNotFoundException(Throwable cause) {
        super(cause);
    }

    public PointNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PointNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
