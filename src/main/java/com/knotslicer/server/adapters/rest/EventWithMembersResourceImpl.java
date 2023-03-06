package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.EventMemberDto;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/{eventId}/members")
@RequestScoped
public class EventWithMembersResourceImpl implements EventWithMembersResource {

    @PATCH
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response addMember(EventMemberDto eventMemberDto) {
        return null;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getWithMembers(@PathParam("eventId")Long eventId) {
        return null;
    }

    @DELETE
    @Path("/{eventId}/members/{memberId}")
    @Override
    public Response removeMember(@PathParam("eventId")Long eventId,
                                 @PathParam("memberId")Long memberId) {
        return null;
    }
}
