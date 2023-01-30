package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface MemberResource {
    Response createMember(MemberDto memberDto, Long userId, UriInfo uriInfo);
    Response getMember(Long memberId, Long userId, UriInfo uriInfo);
    Response updateMember(ProjectDto projectRequestDto, Long projectId, Long userId, UriInfo uriInfo);
    Response deleteProject(Long projectId, Long userId);
}
