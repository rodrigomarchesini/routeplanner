package com.walmart.routeplanner.web.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import com.walmart.routeplanner.services.map.path.exception.InvalidInputException;
import com.walmart.routeplanner.web.model.GenericResponse;

/**
 * Handler for InvalidInputException.
 * 
 * @author Rodrigo Marchesini
 */
@Provider 
@Component
public class InvalidInputExceptionHandler implements ExceptionMapper<InvalidInputException> {

    @Override
    public Response toResponse(InvalidInputException exception) {
        return Response
                .status(Status.BAD_REQUEST)
                .entity(GenericResponse.as(exception.getMessage()))
                .build();
    }

}
