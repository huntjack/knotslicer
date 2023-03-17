package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface ProjectResource {
    Response create(@Valid ProjectDto projectDto, UriInfo uriInfo);
    Response get(Long projectId, UriInfo uriInfo);
    Response update(@Valid ProjectDto projectDto,
                    Long projectId,
                    UriInfo uriInfo);
    Response delete(Long projectId);
    Response getWithChildren(Long projectId, UriInfo uriInfo);
}
