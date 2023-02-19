package com.knotslicer.server.ports.interactor.mappers;

import com.knotslicer.server.ports.interactor.datatransferobjects.ErrorDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.ErrorDtoFactory;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class EntityNotFoundExceptionMapper implements ExceptionMapper<EntityNotFoundException> {
    private static final Logger logger
            = LoggerFactory.getLogger(EntityNotFoundExceptionMapper.class);
    private ErrorDtoFactory errorDtoFactory;

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(EntityNotFoundException exception) {
        logger.error("Entity could not be found.", exception);
        ErrorDto errorDto = errorDtoFactory.createErrorDto(404, exception.getMessage());
        return Response.status(Response.Status.NOT_FOUND)
                .entity(errorDto)
                .build();
    }
    @Inject
    public EntityNotFoundExceptionMapper(ErrorDtoFactory errorDtoFactory) {
        this.errorDtoFactory = errorDtoFactory;
    }
    protected EntityNotFoundExceptionMapper(){}
}
