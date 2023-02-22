package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollDto;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

public class PollLinkCommand extends LinkCommand<PollDto> {
    public PollLinkCommand(LinkReceiver linkReceiver, PollDto dto, UriInfo uriInfo) {
        super(linkReceiver, dto, uriInfo);
    }
    @Override
    public URI execute() {
        URI pollUri = linkReceiver
                .getUriForPoll(
                        uriInfo.getBaseUriBuilder(),
                        dto.getPollId());
        dto.addLink(pollUri.toString(), "self");
        URI eventUri = linkReceiver
                .getUriForEvent(
                        uriInfo.getBaseUriBuilder(),
                        dto.getEventId());
        dto.addLink(eventUri.toString(), "event");
        return pollUri;
    }
}
