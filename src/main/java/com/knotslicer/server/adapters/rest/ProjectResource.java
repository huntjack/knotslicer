package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface ProjectResource {
    Response create(@Valid ProjectDto projectDto, UriInfo uriInfo);
    Response get(@Positive Long projectId, UriInfo uriInfo);
    Response getWithChildren(@Positive Long projectId, UriInfo uriInfo);
    Response update(@Valid ProjectDto projectDto,
                    @Positive Long projectId,
                    UriInfo uriInfo);
    Response delete(@Positive Long projectId);
}
