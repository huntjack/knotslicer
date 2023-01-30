package com.knotslicer.server.ports.interactor.mappers;

import com.knotslicer.server.ports.interactor.datatransferobjects.ErrorDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.ErrorDtoFactory;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Exception> {
    private static final Logger logger
            = LoggerFactory.getLogger(GenericExceptionMapper.class);
    @Inject
    private ErrorDtoFactory errorDtoFactory;
    @Override
    public Response toResponse(Exception exception) {
        logger.error("Whoops, something went wrong. Check the console log.", exception);
        ErrorDto errorDto = errorDtoFactory.createErrorDto(500, exception.getMessage());
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(errorDto)
                .build();
    }
}
