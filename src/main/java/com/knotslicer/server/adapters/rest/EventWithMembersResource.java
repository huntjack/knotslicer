package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.EventMemberDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface EventWithMembersResource {
    Response addMember(@Valid EventMemberDto eventMemberDto, @Positive Long eventId);
    Response getWithMembers(@Positive Long eventId, UriInfo uriInfo);
    Response removeMember(@Positive Long eventId, @Positive Long memberId);
}
