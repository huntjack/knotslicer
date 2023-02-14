package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberLightDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProjectWithMembersLinkCommand extends ProjectLinkCommand {
    public ProjectWithMembersLinkCommand(LinkReceiver linkReceiver, ProjectDto projectDto, UriInfo uriInfo) {
        super(linkReceiver, projectDto, uriInfo);
    }
    @Override
    public URI execute() {
        URI projectUri = super.execute();
        List<MemberLightDto> memberLightDtos = dto.getMembers();
        for(MemberLightDto memberLightDto: memberLightDtos) {
            Map<String,Long> primaryKeys = new HashMap<>(3);
            Long userId = memberLightDto.getUserId();
            primaryKeys.put(
                    "userId",
                    userId);
            primaryKeys.put(
                    "memberId",
                    memberLightDto.getMemberId());
            URI memberUri = linkReceiver
                    .getUriForMember(
                            uriInfo.getBaseUriBuilder(),
                            primaryKeys);
            memberLightDto.addLink(
                    memberUri.toString(),
                    "member");
            URI membersUserUri = linkReceiver
                    .getUriForUser(
                            uriInfo.getBaseUriBuilder(),
                            userId);
            memberLightDto.addLink(
                    membersUserUri.toString(),
                    "user");
        }
        return projectUri;
    }
}
