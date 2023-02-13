package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

public class MemberLinkCommand extends LinkCommand<MemberDto> {
    public MemberLinkCommand(LinkReceiver linkReceiver, MemberDto memberDto, UriInfo uriInfo) {
        super(linkReceiver, memberDto, uriInfo);
    }
    @Override
    public URI execute() {
        URI memberUri = linkReceiver.getUriForMember(uriInfo.getBaseUriBuilder(), dto.getMemberId(), dto.getUserId());
        dto.addLink(memberUri.toString(), "self");
        URI projectUri = linkReceiver.getUriForProject(uriInfo.getBaseUriBuilder(), dto.getProjectId(), dto.getProjectOwnerId());
        dto.addLink(projectUri.toString(), "project");
        URI userUri = linkReceiver.getUriForUser(uriInfo.getBaseUriBuilder(), dto.getUserId());
        dto.addLink(userUri.toString(), "user");
        return memberUri;
    }
}
