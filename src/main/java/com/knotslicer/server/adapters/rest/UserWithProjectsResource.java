package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkInvoker;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.WithChildren;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import com.knotslicer.server.ports.interactor.services.UserWithChildrenService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

@Path("/users/{userId}/projects")
@RequestScoped
public class UserWithProjectsResource implements UserWithChildrenResource {
    private UserWithChildrenService userWithProjectsService;
    private LinkCreator<UserLightDto> userWithProjectsLinkCreator;
    private LinkReceiver linkReceiver;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getWithChildren(@PathParam("userId")Long userId, @Context UriInfo uriInfo) {
        UserLightDto userResponseDto =
                userWithProjectsService
                        .getUserWithChildren(userId);
        LinkCommand<UserLightDto> linkCommand =
                userWithProjectsLinkCreator.createLinkCommand(
                        linkReceiver,
                        userResponseDto,
                        uriInfo);
        addLinks(linkCommand);
        return Response.ok()
                .entity(userResponseDto)
                .type("application/json")
                .build();
    }
    private URI addLinks(LinkCommand<UserLightDto> linkCommand) {
        LinkInvoker linkInvoker =
                userWithProjectsLinkCreator.createLinkInvoker(linkCommand);
        return linkInvoker.executeCommand();
    }
    @Inject
    public UserWithProjectsResource(@ProcessAs(ProcessType.USER) @WithChildren(ProcessType.PROJECT)
                                    UserWithChildrenService userWithProjectsService,
                                    @ProcessAs(ProcessType.USER) @WithChildren(ProcessType.PROJECT)
                                    LinkCreator<UserLightDto> userWithProjectsLinkCreator,
                                    LinkReceiver linkReceiver){
        this.userWithProjectsService = userWithProjectsService;
        this.linkReceiver = linkReceiver;
        this.userWithProjectsLinkCreator = userWithProjectsLinkCreator;
    }
    protected UserWithProjectsResource() {}
}
