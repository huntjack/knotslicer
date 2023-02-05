package com.knotslicer.server.adapters.rest.linkcommands;

import jakarta.ws.rs.core.UriInfo;

public interface LinkParamWrapper<D> {
    UriInfo getUriInfo();
    D getDto();
    String getRel();
}
