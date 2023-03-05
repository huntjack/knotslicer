package com.knotslicer.server.adapters.rest.linkgenerator.linkcreators;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.ScheduleLinkCommand;
import com.knotslicer.server.ports.interactor.datatransferobjects.ScheduleDto;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriInfo;

@ProcessAs(ProcessType.SCHEDULE)
@ApplicationScoped
public class ScheduleLinkCreator implements LinkCreator<ScheduleDto> {
    @Override
    public LinkCommand<ScheduleDto> createLinkCommand(LinkReceiver linkReceiver, ScheduleDto scheduleDto, UriInfo uriInfo) {
        return new ScheduleLinkCommand(linkReceiver, scheduleDto, uriInfo);
    }
}
