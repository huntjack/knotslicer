package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import com.knotslicer.server.ports.interactor.services.ProjectService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

@Path("/users/{userId}/projects")
@RequestScoped
public class ProjectResourceImpl implements ProjectResource {
    @Inject
    private ProjectService projectService;
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response createProject(ProjectDto projectRequestDto, @PathParam("userId") Long userId, @Context UriInfo uriInfo) {
        projectRequestDto.setUserId(userId);
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
        String baseUri = uriInfo
                .getBaseUriBuilder()
                .path(UserResourceImpl.class)
                .build()
                .toString();
        String secondHalfOfUri = "/{userId}/projects/{projectId}";
        String template = baseUri + secondHalfOfUri;
        Map<String, Long> parameters = new HashMap<>();
        parameters.put(
                "userId",
                projectResponseDto.getUserId());
        parameters.put(
                "projectId",
                projectResponseDto.getProjectId());
        UriBuilder uriBuilder = UriBuilder.fromPath(template);
        return uriBuilder
                .buildFromMap(parameters);
    }
    private URI getUriForUser(UriInfo uriInfo, ProjectDto projectResponseDto) {
        return uriInfo.getBaseUriBuilder()
                .path(UserResourceImpl.class)
                .path(Long.toString(projectResponseDto.getUserId()))
                .build();
    }
    @GET
    @Path("/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getProject(@PathParam("projectId") Long projectId,
                               @PathParam("userId") Long userId,
                               @Context UriInfo uriInfo) {
        ProjectDto projectDto = projectService.getProject(projectId, userId);
        addLinks(uriInfo, projectDto);
        return Response.ok()
                .entity(projectDto)
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
                                  @PathParam("userId") Long userId,
                                  @Context UriInfo uriInfo) {
        projectRequestDto.setProjectId(projectId);
        projectRequestDto.setUserId(userId);
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
    public Response deleteProject(@PathParam("projectId")Long projectId, @PathParam("userId") Long userId) {
        projectService.deleteUser(projectId, userId);
        return Response.noContent()
                .build();
    }
}
