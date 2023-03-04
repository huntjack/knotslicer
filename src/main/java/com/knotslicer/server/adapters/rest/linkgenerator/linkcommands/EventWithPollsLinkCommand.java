package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollDto;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class EventWithPollsLinkCommand extends EventLinkCommand {
    public EventWithPollsLinkCommand(LinkReceiver linkReceiver, EventDto dto, UriInfo uriInfo) {
        super(linkReceiver, dto, uriInfo);
    }

    @Override
    public URI execute() {
        URI eventUri = super.execute();
        List<PollDto> pollDtos = dto.getPolls();
        for(PollDto pollDto: pollDtos) {
            URI pollUri = linkReceiver.getUriForPoll(
                    uriInfo.getBaseUriBuilder(),
                    pollDto.getPollId());
            pollDto.addLink(
                    pollUri.toString(),
                    "poll");
        }
        return eventUri;
    }
}
