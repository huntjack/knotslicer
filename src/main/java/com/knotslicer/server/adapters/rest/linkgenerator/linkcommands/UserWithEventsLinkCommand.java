package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class UserWithEventsLinkCommand extends UserLinkCommand {
    public UserWithEventsLinkCommand(LinkReceiver linkReceiver, UserLightDto userLightDto, UriInfo uriInfo) {
        super(linkReceiver, userLightDto, uriInfo);
    }
    @Override
    public URI execute() {
        URI userUri = super.execute();
        List<EventDto> eventDtos = dto.getEvents();
        for(EventDto eventDto :eventDtos) {
            URI eventUri = linkReceiver
                    .getUriForEvent(
                            uriInfo.getBaseUriBuilder(),
                            eventDto.getEventId());
            eventDto.addLink(
                    eventUri.toString(),
                    "event");
        }
        return userUri;
    }
}
