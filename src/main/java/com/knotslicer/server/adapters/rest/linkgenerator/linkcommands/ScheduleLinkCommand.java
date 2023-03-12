package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.ScheduleDto;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;

public class ScheduleLinkCommand extends LinkCommand<ScheduleDto> {
    public ScheduleLinkCommand(LinkReceiver linkReceiver, ScheduleDto scheduleDto, UriInfo uriInfo) {
        super(linkReceiver, scheduleDto, uriInfo);
    }
    @Override
    public URI execute() {
        URI scheduleUri = linkReceiver.getUriForSchedule(
                uriInfo.getBaseUriBuilder(),
                dto.getScheduleId(),
                dto.getMemberId());
        dto.addLink(
                scheduleUri.toString(),
                "self");
        URI memberUri = linkReceiver.getUriForMember(
                uriInfo.getBaseUriBuilder(),
                dto.getMemberId());
        dto.addLink(memberUri.toString(), "member");
        return scheduleUri;
    }
}
