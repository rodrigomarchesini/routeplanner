package com.walmart.routeplanner.web.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import com.walmart.routeplanner.domain.services.PointNotFoundException;
import com.walmart.routeplanner.web.model.GenericResponse;

/**
 * Handler for PointNotFoundException.
 * 
 * @author Rodrigo Marchesini
 */
@Provider 
@Component
public class PointNotFoundExceptionHandler implements ExceptionMapper<PointNotFoundException> {

    @Override
    public Response toResponse(PointNotFoundException exception) {
        return Response
                .status(Status.NOT_FOUND)
                .entity(GenericResponse.as(exception.getMessage()))
                .build();
    }

}
