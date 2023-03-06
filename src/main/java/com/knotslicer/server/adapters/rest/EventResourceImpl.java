package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.Invoker;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.WithChildren;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
import com.knotslicer.server.ports.interactor.services.EventService;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

@Path("/events")
@RequestScoped
public class EventResourceImpl implements EventResource {
    private EventService eventService;
    private LinkCreator<EventDto> linkCreator;
    private LinkCreator<EventDto> eventWithPollsLinkCreator;
    private LinkReceiver linkReceiver;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response create(EventDto eventRequestDto,
                           @Context UriInfo uriInfo) {
        EventDto eventResponseDto = eventService.create(eventRequestDto);
        LinkCommand<EventDto> linkCommand =
                linkCreator.createLinkCommand(
                        linkReceiver,
                        eventResponseDto,
                        uriInfo);
        URI selfUri = addLinks(linkCommand);
        return Response.created(selfUri)
                .entity(eventResponseDto)
                .type("application/json")
                .build();
    }
    private URI addLinks(LinkCommand<EventDto> linkCommand) {
        Invoker invoker =
                linkCreator.createInvoker(linkCommand);
        return invoker.executeCommand();
    }
    @GET
    @Path("/{eventId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response get(@PathParam("eventId") Long eventId,
                        @Context UriInfo uriInfo) {
        EventDto eventResponseDto = eventService.get(eventId);
        LinkCommand<EventDto> linkCommand =
                linkCreator.createLinkCommand(
                        linkReceiver,
                        eventResponseDto,
                        uriInfo);
        addLinks(linkCommand);
        return Response.ok()
                .entity(eventResponseDto)
                .type("application/json")
                .build();
    }
    @GET
    @Path("/{eventId}/polls")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getWithChildren(@PathParam("eventId") Long eventId,
                                   @Context UriInfo uriInfo) {
        EventDto eventResponseDto = eventService.getWithChildren(eventId);
        LinkCommand<EventDto> linkCommand =
                eventWithPollsLinkCreator.createLinkCommand(
                        linkReceiver,
                        eventResponseDto,
                        uriInfo);
        addLinks(linkCommand);
        return Response.ok()
                .entity(eventResponseDto)
                .type("application/json")
                .build();
    }

    @PATCH
    @Path("/{eventId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response update(EventDto eventRequestDto,
                           @PathParam("eventId") Long eventId,
                           @Context UriInfo uriInfo) {
        eventRequestDto.setEventId(eventId);
        EventDto eventResponseDto = eventService.update(eventRequestDto);
        LinkCommand<EventDto> linkCommand =
                linkCreator.createLinkCommand(
                        linkReceiver,
                        eventResponseDto,
                        uriInfo);
        addLinks(linkCommand);
        return Response.ok()
                .entity(eventResponseDto)
                .type("application/json")
                .build();
    }

    @DELETE
    @Path("/{eventId}")
    @Override
    public Response delete(@PathParam("eventId")Long eventId) {
        eventService.delete(eventId);
        return Response
                .noContent()
                .build();
    }
    @Inject
    public EventResourceImpl(EventService eventService,
                             @ProcessAs(ProcessType.EVENT) @Default
                             LinkCreator<EventDto> linkCreator,
                             @ProcessAs(ProcessType.EVENT) @WithChildren
                                 LinkCreator<EventDto> eventWithPollsLinkCreator,
                             LinkReceiver linkReceiver) {
        this.eventService = eventService;
        this.linkCreator = linkCreator;
        this.eventWithPollsLinkCreator = eventWithPollsLinkCreator;
        this.linkReceiver = linkReceiver;
    }
    protected EventResourceImpl() {}
}
