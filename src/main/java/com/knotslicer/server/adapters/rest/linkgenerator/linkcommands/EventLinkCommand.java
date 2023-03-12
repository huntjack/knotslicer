package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;

public class EventLinkCommand extends LinkCommand<EventDto> {
    public EventLinkCommand(LinkReceiver linkReceiver, EventDto eventDto, UriInfo uriInfo) {
        super(linkReceiver, eventDto, uriInfo);
    }
    @Override
    public URI execute() {
        URI eventUri = linkReceiver
                .getUriForEvent(
                        uriInfo.getBaseUriBuilder(),
                        dto.getEventId());
        dto.addLink(
                eventUri.toString(),
                "self");
        URI userUri = linkReceiver
                .getUriForUser(
                        uriInfo.getBaseUriBuilder(),
                        dto.getUserId());
        dto.addLink(
                userUri.toString(),
                "user");
        return eventUri;
    }
}
