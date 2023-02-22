package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.PollLinkCommand;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriInfo;

@PollLinkCreator
@ApplicationScoped
public class PollLinkCreatorImpl implements LinkCreator<PollDto> {
    @Override
    public LinkCommand<PollDto> createLinkCommand(LinkReceiver linkReceiver, PollDto pollDto, UriInfo uriInfo) {
        return new PollLinkCommand(linkReceiver, pollDto, uriInfo);
    }
}
