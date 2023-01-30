package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface ProjectResource {
    Response createProject(ProjectDto projectRequestDto,Long userId, UriInfo uriInfo);
    Response getProject(Long projectId, Long userId, UriInfo uriInfo);
    Response updateProject(ProjectDto projectRequestDto, Long projectId, Long userId, UriInfo uriInfo);
    Response deleteProject(Long projectId, Long userId);
}
