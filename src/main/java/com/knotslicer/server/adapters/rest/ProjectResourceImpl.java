package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.Invoker;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.WithChildren;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import com.knotslicer.server.ports.interactor.services.ParentService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;

@Path("/projects")
@RequestScoped
public class ProjectResourceImpl implements ProjectResource {
    private ParentService<ProjectDto> projectService;
    private LinkCreator<ProjectDto> linkCreator;
    private LinkCreator<ProjectDto> projectWithMembersLinkCreator;
    private LinkReceiver linkReceiver;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response create(ProjectDto projectRequestDto,
                           @Context UriInfo uriInfo) {
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
    private URI addLinks(LinkCommand<ProjectDto> linkCommand) {
        Invoker invoker =
                linkCreator.createInvoker(linkCommand);
        return invoker.executeCommand();
    }
    @GET
    @Path("/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response get(@PathParam("projectId") Long projectId,
                        @Context UriInfo uriInfo) {
        ProjectDto projectResponseDto = projectService.get(projectId);
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
    @GET
    @Path("/{projectId}/members")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getWithChildren(@PathParam("projectId") Long projectId,
                                    @Context UriInfo uriInfo) {
        ProjectDto projectResponseDto = projectService.getWithChildren(projectId);
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
    @PATCH
    @Path("/{projectId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response update(ProjectDto projectRequestDto,
                           @PathParam("projectId") Long projectId,
                           @Context UriInfo uriInfo) {
        projectRequestDto.setProjectId(projectId);
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
    public Response delete(@PathParam("projectId") Long projectId) {
        projectService.delete(projectId);
        return Response
                .noContent()
                .build();
    }
    @Inject
    public ProjectResourceImpl(@ProcessAs(ProcessType.PROJECT)
                                   ParentService<ProjectDto> projectService,
                               @ProcessAs(ProcessType.PROJECT) @Default
                               LinkCreator<ProjectDto> linkCreator,
                               @WithChildren @ProcessAs(ProcessType.MEMBER)
                                   LinkCreator<ProjectDto> projectWithMembersLinkCreator,
                               LinkReceiver linkReceiver) {
        this.projectService = projectService;
        this.linkCreator = linkCreator;
        this.projectWithMembersLinkCreator = projectWithMembersLinkCreator;
        this.linkReceiver = linkReceiver;
    }
    protected ProjectResourceImpl(){}
}
