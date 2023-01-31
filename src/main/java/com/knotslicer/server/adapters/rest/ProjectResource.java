package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface ProjectResource {
    Response createProject(ProjectDto projectRequestDto, UriInfo uriInfo);
    Response getProject(Long projectId, UriInfo uriInfo);
    Response getProjectWithMembers(Long projectId, UriInfo uriInfo);
    Response updateProject(ProjectDto projectRequestDto, Long projectId, UriInfo uriInfo);
    Response deleteProject(Long projectId);
}
