package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
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
        List<MemberDto> memberDtos = dto.getMembers();
        for(MemberDto memberDto : memberDtos) {
            URI memberUri = linkReceiver
                    .getUriForMember(
                            uriInfo.getBaseUriBuilder(),
                            memberDto.getMemberId());
            memberDto.addLink(
                    memberUri.toString(),
                    "member");
            URI membersUserUri = linkReceiver
                    .getUriForUser(
                            uriInfo.getBaseUriBuilder(),
                            memberDto.getUserId());
            memberDto.addLink(
                    membersUserUri.toString(),
                    "user");
        }
        return projectUri;
    }
}
