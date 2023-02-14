package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.ScheduleDto;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class ScheduleLinkCommand extends LinkCommand<ScheduleDto> {
    public ScheduleLinkCommand(LinkReceiver linkReceiver, ScheduleDto dto, UriInfo uriInfo) {
        super(linkReceiver, dto, uriInfo);
    }
    @Override
    public URI execute() {
        Map<String, Long> primaryKeys = new HashMap<>(5);
        primaryKeys.put(
                "userId",
                dto.getUserId());
        primaryKeys.put(
                "memberId",
                dto.getMemberId());
        primaryKeys.put(
                "scheduleId",
                dto.getScheduleId());
        URI scheduleUri = linkReceiver.getUriForSchedule(
                uriInfo.getBaseUriBuilder(),
                primaryKeys);
        dto.addLink(scheduleUri.toString(), "self");
        return scheduleUri;
    }
}
