package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.UserDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

public interface LinkReceiver {
    public URI getUriForUser(UriInfo uriInfo, UserDto userDto);
    public URI getUriForUser(UriInfo uriInfo, UserLightDto userLightDto);
}
