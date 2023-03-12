package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.Invoker;
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

@Path("/users/{userId}/events")
@RequestScoped
public class UserWithEventsResource implements UserWithChildrenResource {
    private UserWithChildrenService userWithEventsService;
    private LinkCreator<UserLightDto> userWithEventsLinkCreator;
    private LinkReceiver linkReceiver;
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getWithChildren(@PathParam("userId")Long userId, @Context UriInfo uriInfo) {
        UserLightDto userResponseDto =
                userWithEventsService
                        .getUserWithChildren(userId);
        LinkCommand<UserLightDto> linkCommand =
                userWithEventsLinkCreator.createLinkCommand(
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
        Invoker invoker =
                userWithEventsLinkCreator.createInvoker(linkCommand);
        return invoker.executeCommand();
    }
    @Inject
    public UserWithEventsResource(@ProcessAs(ProcessType.USER) @WithChildren(ProcessType.EVENT)
                                    UserWithChildrenService userWithEventsService,
                                  @ProcessAs(ProcessType.USER) @WithChildren(ProcessType.EVENT)
                                    LinkCreator<UserLightDto> userWithEventsLinkCreator,
                                    LinkReceiver linkReceiver){
        this.userWithEventsService = userWithEventsService;
        this.linkReceiver = linkReceiver;
        this.userWithEventsLinkCreator = userWithEventsLinkCreator;
    }
    protected UserWithEventsResource() {}
}
