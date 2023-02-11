package com.knotslicer.server.adapters.rest.linkgenerator;

import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;

public class InvokerImpl implements Invoker {
    private LinkCommand linkCommand;
    public InvokerImpl(LinkCommand linkCommand) {
        this.linkCommand = linkCommand;
    }
    @Override
    public void executeCommand() {
        linkCommand.execute();
    }
}
