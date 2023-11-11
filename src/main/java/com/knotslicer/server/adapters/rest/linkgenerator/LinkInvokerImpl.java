package com.knotslicer.server.adapters.rest.linkgenerator;

import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import java.net.URI;

public class LinkInvokerImpl implements LinkInvoker {
    private LinkCommand<?> linkCommand;
    public LinkInvokerImpl(LinkCommand<?> linkCommand) {
        this.linkCommand = linkCommand;
    }
    @Override
    public URI executeCommand() {
        return linkCommand.execute();
    }
}
