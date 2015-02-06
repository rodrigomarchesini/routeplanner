package com.walmart.routeplanner.web.model;

import java.io.Serializable;

/**
 * Generic service response.
 * 
 * @author Rodrigo Marchesini
 */
public class GenericResponse implements Serializable {

    private static final long serialVersionUID = -1849700211083019052L;

    private final String detail;

    public GenericResponse(String detail) {
        this.detail = detail;
    }

    public String getDetail() {
        return detail;
    }
    
    public static GenericResponse as(String detail) {
        return new GenericResponse(detail);
    }

}
