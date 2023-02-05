package com.knotslicer.server.adapters.rest.linkcommands;

import jakarta.ws.rs.core.UriInfo;

public class LinkParamWrapperImpl<D> {
    private UriInfo uriInfo;
    private D dto;
    private String rel;
    LinkParamWrapperImpl(UriInfo uriInfo, D dto, String rel) {
        this.uriInfo = uriInfo;
        this.dto = dto;
        this.rel = rel;
    }
    public UriInfo getUriInfo() {return uriInfo;}
    public D getDto() {return dto;}
    public String getRel() {return rel;}

}
