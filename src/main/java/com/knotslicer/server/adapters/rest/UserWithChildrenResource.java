package com.knotslicer.server.adapters.rest;

import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface UserWithChildrenResource {
    Response getWithChildren(@Positive Long userId, UriInfo uriInfo);
}
