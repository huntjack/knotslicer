package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class UserWithMembersLinkCommand extends UserLinkCommand {
    public UserWithMembersLinkCommand(LinkReceiver linkReceiver, UserLightDto userLightDto, UriInfo uriInfo) {
        super(linkReceiver, userLightDto, uriInfo);
    }
    @Override
    public URI execute() {
        URI userUri = super.execute();
        List<MemberDto> memberDtos = dto.getMembers();
        for(MemberDto memberDto :memberDtos) {
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
        }
        return userUri;
    }
}
