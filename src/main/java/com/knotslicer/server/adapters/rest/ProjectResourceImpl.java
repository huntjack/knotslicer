package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.Invoker;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.ProjectLinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.ProjectWithMembersLinkCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import com.knotslicer.server.ports.interactor.services.ProjectService;
import com.knotslicer.server.ports.interactor.services.ParentService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Path("/users/{userId}/projects")
@RequestScoped
public class ProjectResourceImpl implements ProjectResource {
    @Inject
    @ProjectService
    private ParentService<ProjectDto> projectService;
    @Inject
    @ProjectLinkCreator
    private LinkCreator<ProjectDto> linkCreator;
    @Inject
    @ProjectWithMembersLinkCreator
    private LinkCreator<ProjectDto> projectWithMembersLinkCreator;
    @Inject
    private LinkReceiver linkReceiver;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response create(ProjectDto projectRequestDto,
                           @PathParam("userId") Long userId,
                           @Context UriInfo uriInfo) {
        projectRequestDto.setUserId(userId);
        ProjectDto projectResponseDto = projectService.create(projectRequestDto);
        LinkCommand<ProjectDto> linkCommand =
                linkCreator.createLinkCommand(
                        linkReceiver,
                        projectResponseDto,
                        uriInfo);
        URI selfUri = addLinks(linkCommand);
        return Response.created(selfUri)
                .entity(projectResponseDto)
                .type("application/json")
                .build();
    }
    private URI addLinks(LinkCommand linkCommand) {
        Invoker invoker =
                linkCreator.createInvoker(linkCommand);
        return invoker.executeCommand();
    }
    @GET
    @Path("/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response get(@PathParam("projectId") Long projectId,
                        @PathParam("userId") Long userId,
                        @Context UriInfo uriInfo) {
        Map<String,Long> ids = packIds(projectId, userId);
        ProjectDto projectResponseDto = projectService.get(ids);
        LinkCommand<ProjectDto> linkCommand =
                linkCreator.createLinkCommand(
                        linkReceiver,
                        projectResponseDto,
                        uriInfo);
        addLinks(linkCommand);
        return Response.ok()
                .entity(projectResponseDto)
                .type("application/json")
                .build();
    }
    private Map<String,Long> packIds(Long projectId, Long userId) {
        Map<String,Long> ids = new HashMap<>();
        ids.put("projectId", projectId);
        ids.put("userId", userId);
        return ids;
    }
    @GET
    @Path("/{projectId}/members")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getWithMembers(@PathParam("projectId") Long projectId,
                                    @PathParam("userId") Long userId,
                                    @Context UriInfo uriInfo) {
        Map<String,Long> ids = packIds(projectId, userId);
        ProjectDto projectResponseDto = projectService.getWithChildren(ids);
        LinkCommand<ProjectDto> linkCommand =
                projectWithMembersLinkCreator.createLinkCommand(
                        linkReceiver,
                        projectResponseDto,
                        uriInfo);
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
    public Response update(ProjectDto projectRequestDto,
                           @PathParam("projectId") Long projectId,
                           @PathParam("userId") Long userId,
                           @Context UriInfo uriInfo) {
        projectRequestDto.setProjectId(projectId);
        projectRequestDto.setUserId(userId);
        ProjectDto projectResponseDto =
                projectService.update(projectRequestDto);
        LinkCommand<ProjectDto> linkCommand =
                linkCreator.createLinkCommand(
                        linkReceiver,
                        projectResponseDto,
                        uriInfo);
        addLinks(linkCommand);
        return Response.ok()
                .entity(projectResponseDto)
                .type("application/json")
                .build();
    }
    @DELETE
    @Path("/{projectId}")
    @Override
    public Response delete(@PathParam("projectId") Long projectId,
                           @PathParam("userId") Long userId) {
        Map<String,Long> ids = packIds(projectId, userId);
        projectService.delete(ids);
        return Response
                .noContent()
                .build();
    }
}
