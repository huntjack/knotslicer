package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.WithChildren;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.EventWithMembersLinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriInfo;

@WithChildren
@ProcessAs(ProcessType.MEMBER)
@ApplicationScoped
public class EventWithMembersLinkCreator implements LinkCreator<EventDto> {

    @Override
    public LinkCommand<EventDto> createLinkCommand(LinkReceiver linkReceiver, EventDto eventDto, UriInfo uriInfo) {
        return new EventWithMembersLinkCommand(linkReceiver, eventDto, uriInfo);
    }
}
