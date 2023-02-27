package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.WithChildren;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.PollWithPollAnswersLinkCommand;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriInfo;

@ProcessAs(ProcessType.POLL)
@WithChildren
@ApplicationScoped
public class PollWithPollAnswersLinkCreator implements LinkCreator<PollDto> {
    @Override
    public LinkCommand<PollDto> createLinkCommand(LinkReceiver linkReceiver, PollDto pollDto, UriInfo uriInfo) {
        return new PollWithPollAnswersLinkCommand(linkReceiver, pollDto, uriInfo);
    }
}
