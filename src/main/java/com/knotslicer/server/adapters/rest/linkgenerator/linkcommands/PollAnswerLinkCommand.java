package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollAnswerDto;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;

public class PollAnswerLinkCommand extends LinkCommand<PollAnswerDto> {
    public PollAnswerLinkCommand(LinkReceiver linkReceiver, PollAnswerDto pollAnswerDto, UriInfo uriInfo) {
        super(linkReceiver, pollAnswerDto, uriInfo);
    }
    @Override
    public URI execute() {
        URI pollAnswerUri = linkReceiver.getUriForPollAnswer(
                uriInfo.getBaseUriBuilder(),
                dto.getPollAnswerId(),
                dto.getPollId());
        dto.addLink(
                pollAnswerUri.toString(),
                "self");
        URI pollUri = linkReceiver.getUriForPoll(
                uriInfo.getBaseUriBuilder(),
                dto.getPollId());
        dto.addLink(
                pollUri.toString(),
                "poll");
        URI memberUri = linkReceiver.getUriForMember(
                uriInfo.getBaseUriBuilder(),
                dto.getMemberId());
        dto.addLink(
                memberUri.toString(),
                "member");
        return pollAnswerUri;
    }
}
