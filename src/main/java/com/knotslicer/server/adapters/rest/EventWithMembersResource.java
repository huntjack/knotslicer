package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.EventMemberDto;
import jakarta.ws.rs.core.Response;

public interface EventWithMembersResource {
    Response addMember(EventMemberDto eventMemberDto);
    Response getWithMembers(Long eventId);
    Response removeMember(Long eventId, Long memberId);
}
