package com.knotslicer.server.adapters.rest.linkgenerator;

import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;

import java.net.URI;

public class InvokerImpl implements Invoker {
    private LinkCommand linkCommand;
    public InvokerImpl(LinkCommand linkCommand) {
        this.linkCommand = linkCommand;
    }
    @Override
    public URI executeCommand() {
        return linkCommand.execute();
    }
}
