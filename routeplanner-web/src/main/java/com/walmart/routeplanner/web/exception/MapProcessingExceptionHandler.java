package com.walmart.routeplanner.web.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import com.walmart.routeplanner.services.map.processor.exception.MapProcessingException;
import com.walmart.routeplanner.web.model.GenericResponse;

/**
 * Handler for MapProcessingException.
 * 
 * @author Rodrigo Marchesini
 */
@Provider
@Component
public class MapProcessingExceptionHandler implements ExceptionMapper<MapProcessingException> {

    @Override
    public Response toResponse(MapProcessingException exception) {
        return Response
                .status(Status.BAD_REQUEST)
                .entity(GenericResponse.as(exception.getMessage()))
                .build();
    }

}
