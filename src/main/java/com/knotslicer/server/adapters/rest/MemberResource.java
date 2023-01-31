package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface MemberResource {
    Response createMember(MemberDto memberRequestDto, Long userId, UriInfo uriInfo);
    Response getMember(Long memberId, Long userId, UriInfo uriInfo);
    Response updateMember(MemberDto memberRequestDto, Long userId, UriInfo uriInfo);
    Response deleteProject(Long memberId, Long userId);
}
