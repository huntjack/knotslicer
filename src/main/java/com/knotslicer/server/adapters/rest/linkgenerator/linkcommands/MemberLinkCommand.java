package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

public class MemberLinkCommand extends LinkCommand {
    protected MemberDto memberDto;
    protected UriInfo uriInfo;
    public MemberLinkCommand(LinkReceiver linkReceiver, MemberDto memberDto, UriInfo uriInfo) {
        super(linkReceiver);
        this.memberDto = memberDto;
        this.uriInfo = uriInfo;
    }
    @Override
    public void execute() {
        URI memberUri = linkReceiver.getUriForMembers(uriInfo.getBaseUriBuilder(), memberDto.getMemberId(), memberDto.getUserId());
        this.selfLink = memberUri;
        memberDto.addLink(memberUri.toString(), "self");
        URI projectUri = linkReceiver.getUriForProject(uriInfo.getBaseUriBuilder(), memberDto.getProjectId(), memberDto.getUserId());
        memberDto.addLink(projectUri.toString(), "project");
        URI userUri = linkReceiver.getUriForUser(uriInfo.getBaseUriBuilder(), memberDto.getUserId());
        memberDto.addLink(userUri.toString(), "user");
    }
}
