package com.knotslicer.server.ports.interactor.mappers;

import com.knotslicer.server.ports.interactor.datatransferobjects.ErrorDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.ErrorDtoFactory;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {
    @Inject
    private ErrorDtoFactory errorDtoFactory;
    @Override
    public Response toResponse(Exception exception) {
        //Add logging here
        ErrorDto errorDto = errorDtoFactory.createErrorDto(500, exception.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorDto)
                .build();
    }
}
