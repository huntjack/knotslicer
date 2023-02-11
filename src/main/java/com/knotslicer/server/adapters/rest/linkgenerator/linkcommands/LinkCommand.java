package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;

import java.net.URI;

public abstract class LinkCommand {
    protected LinkReceiver linkReceiver;
    protected URI selfLink;
    public abstract void execute();
    public LinkCommand(LinkReceiver linkReceiver) {
        this.linkReceiver = linkReceiver;
    }
    public URI getSelfLink() {
        return selfLink;
    }
}
