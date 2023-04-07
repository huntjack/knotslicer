package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.UserDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface UserResource {
    Response createUser(@Valid UserDto userRequestDto, UriInfo uriInfo);
    Response getUser(@Positive Long userId, UriInfo uriInfo);
    Response updateUser(@Valid UserLightDto userRequestDto,
                        @Positive Long userId,
                        UriInfo uriInfo);
    Response deleteUser(@Positive Long userId);

}
