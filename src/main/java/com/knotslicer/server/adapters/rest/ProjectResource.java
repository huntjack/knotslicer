package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface ProjectResource {
    Response create(ProjectDto projectDto, Long parentId, UriInfo uriInfo);
    Response get(Long id, Long parentId, UriInfo uriInfo);
    Response update(ProjectDto projectDto, Long id, Long parentId, UriInfo uriInfo);
    Response delete(Long id, Long parentId);
    Response getWithMembers(Long id, Long parentId, UriInfo uriInfo);
}
