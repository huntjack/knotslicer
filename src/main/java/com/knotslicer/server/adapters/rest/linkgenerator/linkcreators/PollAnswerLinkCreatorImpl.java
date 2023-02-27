package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.PollAnswerLinkCommand;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollAnswerDto;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.ws.rs.core.UriInfo;

@ProcessAs(ProcessType.POLLANSWER)
@Default
@ApplicationScoped
public class PollAnswerLinkCreatorImpl implements LinkCreator<PollAnswerDto> {
    @Override
    public LinkCommand<PollAnswerDto> createLinkCommand(LinkReceiver linkReceiver, PollAnswerDto pollAnswerDto, UriInfo uriInfo) {
        return new PollAnswerLinkCommand(linkReceiver, pollAnswerDto, uriInfo);
    }
}
