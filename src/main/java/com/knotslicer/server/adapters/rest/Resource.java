package com.knotslicer.server.adapters.rest;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface Resource<T> {
    Response create(T t, Long parentId, UriInfo uriInfo);
    Response get(Long id, Long parentId, UriInfo uriInfo);
    Response getWithChildren(Long id, Long parentId, UriInfo uriInfo);
    Response update(T t, Long id, Long parentId, UriInfo uriInfo);
    Response delete(Long id, Long parentId);
}
