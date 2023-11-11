package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class MemberWithEventsLinkCommand extends MemberLinkCommand {
    public MemberWithEventsLinkCommand(LinkReceiver linkReceiver, MemberDto memberDto, UriInfo uriInfo) {
        super(linkReceiver, memberDto, uriInfo);
    }
    @Override
    public URI execute() {
        URI memberUri = super.execute();
        List<EventDto> eventDtos = dto.getEvents();
        for(EventDto eventDto: eventDtos) {
            URI eventUri = linkReceiver
                    .getUriForEvent(
                            uriInfo.getBaseUriBuilder(),
                            eventDto.getEventId());
            eventDto.addLink(
                    eventUri.toString(),
                    "event");
        }
        return memberUri;
    }
}
