package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.UserDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface UserResource {
    Response createUser(UserDto userRequestDto, UriInfo uriInfo);
    Response getUser(Long userId, UriInfo uriInfo);
    Response updateUser(UserLightDto userRequestDto, Long userId, UriInfo uriInfo);
    Response deleteUser(Long userId);

}
