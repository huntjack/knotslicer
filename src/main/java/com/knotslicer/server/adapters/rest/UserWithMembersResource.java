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

@Path("/users/{userId}/members")
@RequestScoped
public class UserWithMembersResource implements UserWithChildrenResource {
    private UserWithChildrenService userWithMembersService;
    private LinkCreator<UserLightDto> userWithMembersLinkCreator;
    private LinkReceiver linkReceiver;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getWithChildren(@PathParam("userId")Long userId, @Context UriInfo uriInfo) {
        UserLightDto userResponseDto =
                userWithMembersService
                        .getUserWithChildren(userId);
        LinkCommand<UserLightDto> linkCommand =
                userWithMembersLinkCreator.createLinkCommand(
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
                userWithMembersLinkCreator.createInvoker(linkCommand);
        return invoker.executeCommand();
    }
    @Inject
    public UserWithMembersResource (@ProcessAs(ProcessType.USER) @WithChildren(ProcessType.MEMBER)
                                        UserWithChildrenService userWithMembersService,
                                    @ProcessAs(ProcessType.USER) @WithChildren(ProcessType.MEMBER)
                                    LinkCreator<UserLightDto> userWithMembersLinkCreator,
                                    LinkReceiver linkReceiver) {
        this.userWithMembersService = userWithMembersService;
        this.userWithMembersLinkCreator = userWithMembersLinkCreator;
        this.linkReceiver = linkReceiver;
    }
    protected UserWithMembersResource() {}
}
