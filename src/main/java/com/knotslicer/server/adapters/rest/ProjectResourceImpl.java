package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.Invoker;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.ProjectLinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.ProjectWithMembersLinkCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import com.knotslicer.server.ports.interactor.services.ProjectService;
import com.knotslicer.server.ports.interactor.services.Service;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;

@Path("/users/{userId}/projects")
@RequestScoped
public class ProjectResourceImpl implements Resource<ProjectDto> {
    @Inject
    @ProjectService
    private Service<ProjectDto> projectService;
    @Inject
    @ProjectLinkCreator
    LinkCreator<ProjectDto> linkCreator;
    @Inject
    @ProjectWithMembersLinkCreator
    LinkCreator<ProjectDto> projectWithMembersLinkCreator;
    @Inject
    LinkReceiver linkReceiver;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response create(ProjectDto projectRequestDto, @PathParam("userId") Long userId, @Context UriInfo uriInfo) {
        projectRequestDto.setUserId(userId);
        ProjectDto projectResponseDto = projectService.create(projectRequestDto);
        LinkCommand linkCommand = linkCreator.createLinkCommand(linkReceiver, projectResponseDto, uriInfo);
        addLinks(linkCommand);
        URI uri = linkCommand.getSelfLink();
        return Response.created(uri)
                .entity(projectResponseDto)
                .type("application/json")
                .build();
    }
    private void addLinks(LinkCommand linkCommand) {
        Invoker invoker =
                linkCreator.createInvoker(linkCommand);
        invoker.executeCommand();
    }
    @GET
    @Path("/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response get(@PathParam("projectId") Long projectId, @PathParam("userId") Long userId,
                               @Context UriInfo uriInfo) {
        ProjectDto projectResponseDto = projectService.get(projectId, userId);
        LinkCommand linkCommand = linkCreator.createLinkCommand(linkReceiver, projectResponseDto, uriInfo);
        addLinks(linkCommand);
        return Response.ok()
                .entity(projectResponseDto)
                .type("application/json")
                .build();
    }
    @GET
    @Path("/{projectId}/members")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getWithChildren(@PathParam("projectId") Long projectId, @PathParam("userId") Long userId, @Context UriInfo uriInfo) {
        ProjectDto projectResponseDto = projectService.getWithChildren(projectId, userId);
        LinkCommand linkCommand = projectWithMembersLinkCreator.createLinkCommand(linkReceiver, projectResponseDto, uriInfo);
        addLinks(linkCommand);
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
    public Response update(ProjectDto projectRequestDto, @PathParam("projectId") Long projectId, @PathParam("userId") Long userId, @Context UriInfo uriInfo) {
        projectRequestDto.setProjectId(projectId);
        projectRequestDto.setUserId(userId);
        ProjectDto projectResponseDto = projectService.update(projectRequestDto);
        LinkCommand linkCommand = linkCreator.createLinkCommand(linkReceiver, projectResponseDto, uriInfo);
        addLinks(linkCommand);
        return Response.ok()
                .entity(projectResponseDto)
                .type("application/json")
                .build();
    }
    @DELETE
    @Path("/{projectId}")
    @Override
    public Response delete(@PathParam("projectId") Long projectId, @PathParam("userId") Long userId) {
        projectService.delete(projectId, userId);
        return Response.noContent()
                .build();
    }
}
