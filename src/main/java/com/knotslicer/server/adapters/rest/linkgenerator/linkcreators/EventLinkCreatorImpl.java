package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.EventLinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.ws.rs.core.UriInfo;

@ProcessAs(ProcessType.EVENT)
@Default
@ApplicationScoped
public class EventLinkCreatorImpl implements LinkCreator<EventDto> {
    @Override
    public LinkCommand<EventDto> createLinkCommand(LinkReceiver linkReceiver, EventDto eventDto, UriInfo uriInfo) {
        return new EventLinkCommand(linkReceiver, eventDto, uriInfo);
    }
}
