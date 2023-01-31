package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import com.knotslicer.server.ports.interactor.services.ProjectService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Path("/projects")
@RequestScoped
public class ProjectResourceImpl implements ProjectResource {
    @Inject
    private ProjectService projectService;
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response createProject(ProjectDto projectRequestDto, @Context UriInfo uriInfo) {
        ProjectDto projectResponseDto = projectService.createProject(projectRequestDto);
        addLinks(uriInfo, projectResponseDto);
        URI selfUri = getUriForSelf(uriInfo, projectResponseDto);
        return Response.created(selfUri)
                .entity(projectResponseDto)
                .type("application/json")
                .build();
    }
    private void addLinks(UriInfo uriInfo, ProjectDto projectResponseDto) {
        URI selfUri = getUriForSelf(uriInfo,
                projectResponseDto);
        URI userUri = getUriForUser(uriInfo,
                projectResponseDto);
        projectResponseDto.addLink(selfUri.toString(), "self");
        projectResponseDto.addLink(userUri.toString(), "user");
    }
    private URI getUriForSelf(UriInfo uriInfo, ProjectDto projectResponseDto) {
        return uriInfo
                .getBaseUriBuilder()
                .path(ProjectResourceImpl.class)
                .path(Long.toString(projectResponseDto.getProjectId()))
                .build();
    }
    private URI getUriForUser(UriInfo uriInfo, ProjectDto projectResponseDto) {
        return uriInfo
                .getBaseUriBuilder()
                .path(UserResourceImpl.class)
                .path(Long.toString(projectResponseDto.getUserId()))
                .build();
    }
    @GET
    @Path("/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getProject(@PathParam("projectId") Long projectId,
                               @Context UriInfo uriInfo) {
        ProjectDto projectResponseDto = projectService.getProject(projectId);
        addLinks(uriInfo, projectResponseDto);
        return Response.ok()
                .entity(projectResponseDto)
                .type("application/json")
                .build();
    }
    @GET
    @Path("/{projectId}/members")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getProjectWithMembers(@PathParam("projectId") Long projectId, @Context UriInfo uriInfo) {
        ProjectDto projectResponseDto = projectService.getProjectWithMembers(projectId);
        addLinks(uriInfo, projectResponseDto);
        return Response.ok()
                .entity(projectResponseDto)
                .type("application/json")
                .build();
    }
    @PUT
    @Path("/{projectId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response updateProject(ProjectDto projectRequestDto,
                                  @PathParam("projectId") Long projectId,
                                  @Context UriInfo uriInfo) {
        projectRequestDto.setProjectId(projectId);
        ProjectDto projectResponseDto = projectService.updateProject(projectRequestDto);
        addLinks(uriInfo, projectResponseDto);
        return Response.ok()
                .entity(projectResponseDto)
                .type("application/json")
                .build();
    }
    @DELETE
    @Path("/{projectId}")
    @Override
    public Response deleteProject(@PathParam("projectId")Long projectId) {
        projectService.deleteUser(projectId);
        return Response.noContent()
                .build();
    }
}
