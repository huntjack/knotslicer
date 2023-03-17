package com.knotslicer.server.ports.interactor.mappers;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Set;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    private static final Logger logger
            = LoggerFactory.getLogger(ConstraintViolationExceptionMapper.class);
    private UriInfo uriInfo;

    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public Response toResponse(ConstraintViolationException exception) {
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        final JsonArrayBuilder jsonArray = Json.createArrayBuilder();
        for (final ConstraintViolation<?> constraintviolation : constraintViolations) {
            JsonObject jsonViolation = createJsonViolation(constraintviolation);
            jsonArray.add(jsonViolation);
            logger.error(jsonViolation.toString());
        }
        final JsonObjectBuilder resourceInfo = createResourceInfoJsonObjectBuilder();
        JsonObject constraintViolationJsonEntity =
                resourceInfo.add("violations",
                                jsonArray.build())
                        .build();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(constraintViolationJsonEntity)
                .build();
    }
    private JsonObjectBuilder createResourceInfoJsonObjectBuilder() {
        return Json.createObjectBuilder()
                .add("host", uriInfo.getAbsolutePath().getHost())
                .add("resource", uriInfo.getAbsolutePath().getPath())
                .add("title", "constraintViolations");
    }
    private JsonObject createJsonViolation(ConstraintViolation<?> constraint) {
        String message = constraint.getMessage();
        String propertyPath = constraint
                .getPropertyPath()
                .toString()
                .split("\\.")[2];

        return Json.createObjectBuilder()
                .add("property", propertyPath)
                .add("violationMessage", message)
                .build();
    }
    public ConstraintViolationExceptionMapper(@Context UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }
}