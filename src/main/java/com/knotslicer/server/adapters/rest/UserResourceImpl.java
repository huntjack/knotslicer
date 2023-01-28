package com.knotslicer.server.adapters.rest;

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
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResourceImpl implements UserResource {
    @Inject
    private UserService userService;
    @POST
    public Response createUser(UserDto userDto, @Context UriInfo uriInfo) {
        UserDto newUserDto = userService.createUser(userDto);
        URI uri = getUriForSelf(uriInfo, newUserDto);
        newUserDto.addLink(uri.toString(), "self");
        return Response.created(uri)
                .entity(newUserDto)
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
    public Response getUser(@PathParam("userId") Long userId, @Context UriInfo uriInfo) {
        UserDto userDto = userService.getUser(userId);
        String uri = getUriForSelf(uriInfo, userDto).toString();
        userDto.addLink(uri, "self");
        return Response.ok()
                .entity(userDto)
                .type("application/json")
                .build();
    }
    @DELETE
    @Path("/{userId}")
    public Response deleteUser(@PathParam("userId") Long userId) {
        userService.deleteUser(userId);
        return Response.noContent()
                .build();
    }
}
