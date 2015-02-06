package com.walmart.routeplanner.web.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import com.walmart.routeplanner.processor.map.processing.exception.MalformedMapException;
import com.walmart.routeplanner.web.model.GenericResponse;

/**
 * Handler for MalformedMapException.
 * 
 * @author Rodrigo Marchesini
 */
@Provider 
@Component
public class MalformedMapExceptionHandler implements ExceptionMapper<MalformedMapException> {

    @Override
    public Response toResponse(MalformedMapException exception) {
        return Response
                .status(Status.BAD_REQUEST)
                .entity(GenericResponse.as(exception.getMessage()))
                .build();
    }

}
