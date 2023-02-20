package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.ScheduleLinkCommand;
import com.knotslicer.server.ports.interactor.datatransferobjects.ScheduleDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriInfo;

@ScheduleLinkCreator
@ApplicationScoped
public class ScheduleLinkCreatorImpl implements LinkCreator<ScheduleDto> {
    @Override
    public LinkCommand<ScheduleDto> createLinkCommand(LinkReceiver linkReceiver, ScheduleDto scheduleDto, UriInfo uriInfo) {
        return new ScheduleLinkCommand(linkReceiver, scheduleDto, uriInfo);
    }
}
