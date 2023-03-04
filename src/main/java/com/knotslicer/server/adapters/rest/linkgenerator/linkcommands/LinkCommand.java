package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;

public abstract class LinkCommand<D> {

    protected LinkReceiver linkReceiver;
    protected D dto;
    protected UriInfo uriInfo;

    public LinkCommand(LinkReceiver linkReceiver, D dto, UriInfo uriInfo) {
        this.linkReceiver = linkReceiver;
        this.dto = dto;
        this.uriInfo = uriInfo;
    }
    public abstract URI execute();
}
