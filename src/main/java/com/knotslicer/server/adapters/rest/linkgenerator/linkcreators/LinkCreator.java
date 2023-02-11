package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.Invoker;
import com.knotslicer.server.adapters.rest.linkgenerator.InvokerImpl;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import jakarta.ws.rs.core.UriInfo;

public interface LinkCreator<D> {
    LinkCommand createLinkCommand(LinkReceiver linkReceiver, D d, UriInfo uriInfo);
    default Invoker createInvoker(LinkCommand linkCommand) {
        return new InvokerImpl(linkCommand);
    }
}
