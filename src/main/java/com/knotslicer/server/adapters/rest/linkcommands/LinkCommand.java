package com.knotslicer.server.adapters.rest.linkcommands;

import com.knotslicer.server.adapters.rest.LinkReceiver;

public abstract class LinkCommand {
    protected LinkReceiver linkReceiver;
    public abstract void execute();
    public LinkCommand(LinkReceiver linkReceiver) {
        this.linkReceiver = linkReceiver;
    }
}
