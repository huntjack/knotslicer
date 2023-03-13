package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.PollLinkCommand;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollDto;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.ws.rs.core.UriInfo;

@ProcessAs(ProcessType.POLL)
@Default
@ApplicationScoped
public class PollLinkCreator implements LinkCreator<PollDto> {
    @Override
    public LinkCommand<PollDto> createLinkCommand(LinkReceiver linkReceiver, PollDto pollDto, UriInfo uriInfo) {
        return new PollLinkCommand(linkReceiver, pollDto, uriInfo);
    }
}
