package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface ProjectResource {
    Response create(ProjectDto projectDto, UriInfo uriInfo);
    Response get(Long id, UriInfo uriInfo);
    Response update(ProjectDto projectDto, Long id, UriInfo uriInfo);
    Response delete(Long id);
    Response getWithMembers(Long id, UriInfo uriInfo);
}
