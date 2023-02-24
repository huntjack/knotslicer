package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.EventWithPollsLinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
import jakarta.ws.rs.core.UriInfo;

@EventWithPollsLinkCreator
public class EventWithPollsLinkCreatorImpl implements LinkCreator<EventDto> {
    @Override
    public LinkCommand<EventDto> createLinkCommand(LinkReceiver linkReceiver, EventDto eventDto, UriInfo uriInfo) {
        return new EventWithPollsLinkCommand(linkReceiver, eventDto, uriInfo);
    }
}
