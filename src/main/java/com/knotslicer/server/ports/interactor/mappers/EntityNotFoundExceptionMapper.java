package com.knotslicer.server.ports.interactor.mappers;

import com.knotslicer.server.ports.interactor.datatransferobjects.ErrorDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.ErrorDtoFactory;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class EntityNotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {
    @Inject
    private ErrorDtoFactory errorDtoFactory;
    @Override
    public Response toResponse(EntityNotFoundException exception) {
        //Add logging here
        ErrorDto errorDto = errorDtoFactory.createErrorDto(404, exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND)
                .entity(errorDto)
                .build();
    }
}
