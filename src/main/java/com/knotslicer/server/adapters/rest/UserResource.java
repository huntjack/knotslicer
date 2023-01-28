package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.UserDto;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface UserResource {
    Response createUser(UserDto userDto, UriInfo uriInfo);
    Response getUser(Long userId, UriInfo uriInfo);
    Response deleteUser(Long userId);

}
