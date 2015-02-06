package com.walmart.routeplanner.processor.map.path.exception;

/**
 * Thrown to indicate an invalid input.
 * 
 * @author Rodrigo Marchesini
 *
 */
public class InvalidInputException extends RuntimeException {

    private static final long serialVersionUID = 4387084476385839446L;

    public InvalidInputException() {
    }

    public InvalidInputException(String message) {
        super(message);
    }

    public InvalidInputException(Throwable cause) {
        super(cause);
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidInputException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
