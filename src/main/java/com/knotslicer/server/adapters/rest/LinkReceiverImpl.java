package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.UserDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

public class LinkReceiverImpl implements LinkReceiver {
    @Override
    public URI getUriForUser(UriInfo uriInfo, UserDto userDto) {
        return uriInfo.getBaseUriBuilder()
                .path(UserResourceImpl.class)
                .path(Long.toString(userDto.getUserId()))
                .build();
    }
    @Override
    public URI getUriForUser(UriInfo uriInfo, UserLightDto userLightDto) {
        return uriInfo.getBaseUriBuilder()
                .path(UserResourceImpl.class)
                .path(Long.toString(userLightDto.getUserId()))
                .build();
    }
}
