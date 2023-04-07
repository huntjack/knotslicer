package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkInvoker;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkInvokerImpl;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import jakarta.ws.rs.core.UriInfo;

public interface LinkCreator<D> {
    LinkCommand<D> createLinkCommand(LinkReceiver linkReceiver, D d, UriInfo uriInfo);
    default LinkInvoker createLinkInvoker(LinkCommand linkCommand) {
        return new LinkInvokerImpl(linkCommand);
    }
}
