package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberLightDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.List;

public class ProjectWithMembersLinkCommand extends ProjectLinkCommand {
    public ProjectWithMembersLinkCommand(LinkReceiver linkReceiver, ProjectDto projectDto, UriInfo uriInfo) {
        super(linkReceiver, projectDto, uriInfo);
    }
    @Override
    public URI execute() {
        URI projectUri = super.execute();
        List<MemberLightDto> memberLightDtos = dto.getMembers();
        for(MemberLightDto memberLightDto: memberLightDtos) {
            URI memberUri = linkReceiver.getUriForMember(
                    uriInfo.getBaseUriBuilder(),
                    memberLightDto.getMemberId(),
                    memberLightDto.getUserId());
            memberLightDto.addLink(
                    memberUri.toString(),
                    "member");
            URI membersUserUri = linkReceiver.getUriForUser(
                    uriInfo.getBaseUriBuilder(),
                    memberLightDto.getUserId());
            memberLightDto.addLink(
                    membersUserUri.toString(),
                    "user");
        }
        return projectUri;
    }
}
