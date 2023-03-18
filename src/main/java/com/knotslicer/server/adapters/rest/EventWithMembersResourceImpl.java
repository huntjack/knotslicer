package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.Invoker;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.ports.interactor.WithChildren;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventMemberDto;
import com.knotslicer.server.ports.interactor.services.EventService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

@Path("events/{eventId}/members")
@RequestScoped
public class EventWithMembersResourceImpl implements EventWithMembersResource {
    private EventService eventService;
    private LinkCreator<EventDto> eventWithMembersLinkCreator;
    private LinkReceiver linkReceiver;

    @PATCH
    @Override
    public Response addMember(EventMemberDto eventMemberDto,
                              @PathParam("eventId") Long eventId) {
        eventMemberDto.setEventId(eventId);
        eventService.addMember(eventMemberDto);
        return Response
                .noContent()
                .build();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getWithMembers(@PathParam("eventId")Long eventId, @Context UriInfo uriInfo) {
        EventDto eventResponseDto = eventService.getWithMembers(eventId);
        LinkCommand<EventDto> linkCommand =
                eventWithMembersLinkCreator.createLinkCommand(
                        linkReceiver,
                        eventResponseDto,
                        uriInfo);
        addLinks(linkCommand);
        return Response.ok()
                .entity(eventResponseDto)
                .type("application/json")
                .build();
    }
    private URI addLinks(LinkCommand<EventDto> linkCommand) {
        Invoker invoker =
                eventWithMembersLinkCreator.createInvoker(linkCommand);
        return invoker.executeCommand();
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
    public EventWithMembersResourceImpl(EventService eventService,
                                        @ProcessAs(ProcessType.EVENT) @WithChildren(ProcessType.MEMBER)
                                        LinkCreator<EventDto> eventWithMembersLinkCreator,
                                        LinkReceiver linkReceiver) {
        this.eventService = eventService;
        this.eventWithMembersLinkCreator = eventWithMembersLinkCreator;
        this.linkReceiver = linkReceiver;
    }
    protected EventWithMembersResourceImpl() {}
}
