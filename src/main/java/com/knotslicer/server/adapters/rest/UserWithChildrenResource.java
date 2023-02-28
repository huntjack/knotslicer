package com.knotslicer.server.adapters.rest;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface UserWithChildrenResource {
    Response getWithChildren(Long userId, UriInfo uriInfo);
}
