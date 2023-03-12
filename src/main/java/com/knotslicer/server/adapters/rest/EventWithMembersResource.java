package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.EventMemberDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface EventWithMembersResource {
    Response addMember(EventMemberDto eventMemberDto, Long eventId);
    Response getWithMembers(Long eventId, UriInfo uriInfo);
    Response removeMember(Long eventId, Long memberId);
}
