package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class EventWithMembersLinkCommand extends EventLinkCommand {
    public EventWithMembersLinkCommand(LinkReceiver linkReceiver, EventDto eventDto, UriInfo uriInfo) {
        super(linkReceiver, eventDto, uriInfo);
    }
    @Override
    public URI execute() {
        URI eventUri = super.execute();
        List<MemberDto> memberDtos = dto.getMembers();
        for(MemberDto memberDto: memberDtos) {
            URI memberUri = linkReceiver
                    .getUriForMember(
                            uriInfo.getBaseUriBuilder(),
                            memberDto.getMemberId());
            memberDto.addLink(
                    memberUri.toString(),
                    "member");
            URI projectUri = linkReceiver
                    .getUriForProject(
                            uriInfo.getBaseUriBuilder(),
                            memberDto.getProjectId());
            memberDto.addLink(
                    projectUri.toString(),
                    "project");
            URI userUri = linkReceiver
                    .getUriForUser(
                            uriInfo.getBaseUriBuilder(),
                            memberDto.getUserId());
            memberDto.addLink(
                    userUri.toString(),
                    "user");
        }
        return eventUri;
    }
}

