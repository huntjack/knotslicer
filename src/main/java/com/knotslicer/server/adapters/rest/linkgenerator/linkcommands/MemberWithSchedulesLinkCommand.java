package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.ScheduleDto;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class MemberWithSchedulesLinkCommand extends MemberLinkCommand {
    public MemberWithSchedulesLinkCommand(LinkReceiver linkReceiver, MemberDto dto, UriInfo uriInfo) {
        super(linkReceiver, dto, uriInfo);
    }
    @Override
    public URI execute() {
        URI memberUri = super.execute();
        List<ScheduleDto> scheduleDtos = dto.getSchedules();
        Long memberId = dto.getMemberId();
        for(ScheduleDto scheduleDto: scheduleDtos) {
            URI scheduleUri = linkReceiver.getUriForSchedule(
                    uriInfo.getBaseUriBuilder(),
                    scheduleDto.getScheduleId(),
                    memberId);
            scheduleDto.addLink(
                    scheduleUri.toString(),
                    "schedule");
        }
        return memberUri;
    }
}
