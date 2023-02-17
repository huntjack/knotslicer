package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.Invoker;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.UserLinkCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import com.knotslicer.server.ports.interactor.services.UserService;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

@Path("/users")
@RequestScoped
public class UserResourceImpl implements UserResource {
    @Inject
    private UserService userService;
    @Inject
    @UserLinkCreator
    private LinkCreator<UserLightDto> linkCreator;
    @Inject
    private LinkReceiver linkReceiver;
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response createUser(UserDto userRequestDto,
                               @Context UriInfo uriInfo) {
        UserDto userResponseDto = userService.createUser(userRequestDto);
        LinkCommand<UserLightDto> linkCommand =
                linkCreator.createLinkCommand(
                        linkReceiver,
                        userResponseDto,
                        uriInfo);
        URI selfUri = addLinks(linkCommand);
        return Response.created(selfUri)
                .entity(userResponseDto)
                .type("application/json")
                .build();
    }
    private URI addLinks(LinkCommand linkCommand) {
        Invoker invoker =
                linkCreator.createInvoker(linkCommand);
        return invoker.executeCommand();
    }
    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getUser(@PathParam("userId") Long userId,
                            @Context UriInfo uriInfo) {
        UserLightDto userResponseDto = userService.getUser(userId);
        LinkCommand<UserLightDto> linkCommand =
                linkCreator.createLinkCommand(
                        linkReceiver,
                        userResponseDto,
                        uriInfo);
        addLinks(linkCommand);
        return Response.ok()
                .entity(userResponseDto)
                .type("application/json")
                .build();
    }
    @PUT
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response updateUser(UserLightDto userRequestDto,
                               @PathParam("userId") Long userId,
                               @Context UriInfo uriInfo) {
        userRequestDto.setUserId(userId);
        UserLightDto userResponseDto = userService.updateUser(userRequestDto);
        LinkCommand<UserLightDto> linkCommand =
                linkCreator.createLinkCommand(
                        linkReceiver,
                        userResponseDto,
                        uriInfo);
        addLinks(linkCommand);
        return Response.ok()
                .entity(userResponseDto)
                .type("application/json")
                .build();
    }
    @DELETE
    @Path("/{userId}")
    @Override
    public Response deleteUser(@PathParam("userId") Long userId) {
        userService.deleteUser(userId);
        return Response
                .noContent()
                .build();
    }
}
