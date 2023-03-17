package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.UserDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface UserResource {
    Response createUser(@Valid UserDto userRequestDto, UriInfo uriInfo);
    Response getUser(Long userId, UriInfo uriInfo);
    Response updateUser(@Valid UserLightDto userRequestDto,
                        Long userId,
                        UriInfo uriInfo);
    Response deleteUser(Long userId);

}
