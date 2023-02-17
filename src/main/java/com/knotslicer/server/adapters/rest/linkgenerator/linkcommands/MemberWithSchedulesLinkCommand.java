package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.ScheduleDto;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberWithSchedulesLinkCommand extends MemberLinkCommand {
    public MemberWithSchedulesLinkCommand(LinkReceiver linkReceiver, MemberDto memberDto, UriInfo uriInfo) {
        super(linkReceiver, memberDto, uriInfo);
    }
    @Override
    public URI execute() {
        URI memberUri = super.execute();
        List<ScheduleDto> scheduleDtos = dto.getSchedules();
        Map<String,Long> ids = new HashMap<>(5);
        ids.put("memberId",
                dto.getMemberId());
        ids.put("userId",
                dto.getUserId());
        for(ScheduleDto scheduleDto: scheduleDtos) {
            ids.put("scheduleId",
                    scheduleDto.getScheduleId());
            URI scheduleUri = linkReceiver.getUriForSchedule(
                    uriInfo.getBaseUriBuilder(),
                    ids);
            scheduleDto.addLink(
                    scheduleUri.toString(),
                    "schedule");
        }
        return memberUri;
    }
}
