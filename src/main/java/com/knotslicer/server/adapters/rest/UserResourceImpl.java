package com.knotslicer.server.adapters.rest;

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
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response createUser(UserDto userRequestDto, @Context UriInfo uriInfo) {
        UserDto UserResponseDto = userService.createUser(userRequestDto);
        URI uri = getUriForSelf(uriInfo, UserResponseDto);
        UserResponseDto.addLink(uri.toString(), "self");
        return Response.created(uri)
                .entity(UserResponseDto)
                .type("application/json")
                .build();
    }
    private URI getUriForSelf(UriInfo uriInfo, UserDto userDto) {
        return uriInfo.getBaseUriBuilder()
                .path(UserResourceImpl.class)
                .path(Long.toString(userDto.getUserId()))
                .build();
    }
    @GET
    @Path("/{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getUser(@PathParam("userId") Long userId, @Context UriInfo uriInfo) {
        UserLightDto userResponseDto = userService.getUser(userId);
        String uri = getUriForSelf(uriInfo, userResponseDto).toString();
        userResponseDto.addLink(uri, "self");
        return Response.ok()
                .entity(userResponseDto)
                .type("application/json")
                .build();
    }
    private URI getUriForSelf(UriInfo uriInfo, UserLightDto userLightDto) {
        return uriInfo.getBaseUriBuilder()
                .path(UserResourceImpl.class)
                .path(Long.toString(userLightDto.getUserId()))
                .build();
    }
    @PUT
    @Path("/{userId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response updateUser(UserLightDto userRequestDto, @PathParam("userId") Long userId, @Context UriInfo uriInfo) {
        userRequestDto.setUserId(userId);
        UserLightDto userResponseDto = userService.updateUser(userRequestDto);
        String uri = getUriForSelf(uriInfo, userResponseDto).toString();
        userResponseDto.addLink(uri, "self");
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
        return Response.noContent()
                .build();
    }
}
