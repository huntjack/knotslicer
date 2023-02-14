package com.knotslicer.server.adapters.rest.linkgenerator.linkcommands;

import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class MemberLinkCommand extends LinkCommand<MemberDto> {
    public MemberLinkCommand(LinkReceiver linkReceiver, MemberDto memberDto, UriInfo uriInfo) {
        super(linkReceiver, memberDto, uriInfo);
    }
    @Override
    public URI execute() {
        Map<String,Long> primaryKeys = new HashMap<>(6);
        primaryKeys.put(
                "userId",
                dto.getUserId());
        primaryKeys.put(
                "memberId",
                dto.getMemberId());
        primaryKeys.put(
                "projectId",
                dto.getProjectId());
        primaryKeys.put(
                "projectOwnerId",
                dto.getProjectOwnerId());
        URI memberUri = linkReceiver
                .getUriForMember(
                        uriInfo.getBaseUriBuilder(),
                        primaryKeys);
        dto.addLink(
                memberUri.toString(),
                "self");
        URI projectUri = linkReceiver
                .getUriForProject(
                        uriInfo.getBaseUriBuilder(),
                        primaryKeys);
        dto.addLink(
                projectUri.toString(),
                "project");
        URI userUri = linkReceiver
                .getUriForUser(
                        uriInfo.getBaseUriBuilder(),
                        dto.getUserId());
        dto.addLink(
                userUri.toString(),
                "user");
        return memberUri;
    }
}
