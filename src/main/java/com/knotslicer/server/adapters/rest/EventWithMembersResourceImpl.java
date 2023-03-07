package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventMemberDto;
import com.knotslicer.server.ports.interactor.services.EventService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("events/{eventId}/members")
@RequestScoped
public class EventWithMembersResourceImpl implements EventWithMembersResource {
    private EventService eventService;

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response addMember(EventMemberDto eventMemberDto, @PathParam("eventId")Long eventId) {
        eventMemberDto.setEventId(eventId);
        EventDto eventResponseDto = eventService.addMember(eventMemberDto);
        return Response.ok()
                .entity(eventResponseDto)
                .type("application/json")
                .build();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getWithMembers(@PathParam("eventId")Long eventId) {
        return null;
    }

    @DELETE
    @Path("/{memberId}")
    @Override
    public Response removeMember(@PathParam("eventId")Long eventId,
                                 @PathParam("memberId")Long memberId) {
        eventService.removeMember(eventId, memberId);
        return Response
                .noContent()
                .build();
    }
    @Inject
    public EventWithMembersResourceImpl(EventService eventService) {
        this.eventService = eventService;
    }
    protected EventWithMembersResourceImpl() {}
}
