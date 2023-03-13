package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.Invoker;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollDto;
import com.knotslicer.server.ports.interactor.services.ParentService;
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

@Path("/polls")
@RequestScoped
public class PollResourceImpl implements PollResource {
    private ParentService<PollDto> pollService;
    private LinkCreator<PollDto> linkCreator;
    private LinkReceiver linkReceiver;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response create(PollDto pollRequestDto,
                           @Context UriInfo uriInfo) {
        PollDto pollResponseDto = pollService.create(pollRequestDto);
        LinkCommand<PollDto> linkCommand = linkCreator.createLinkCommand(
                linkReceiver,
                pollResponseDto,
                uriInfo);
        URI selfUri = addLinks(linkCommand);
        return Response.created(selfUri)
                .entity(pollResponseDto)
                .type("application/json")
                .build();
    }
    private URI addLinks(LinkCommand<PollDto> linkCommand) {
        Invoker invoker =
                linkCreator.createInvoker(linkCommand);
        return invoker.executeCommand();
    }
    @GET
    @Path("/{pollId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response get(@PathParam("pollId") Long pollId,
                        @Context UriInfo uriInfo) {
        PollDto pollResponseDto = pollService.get(pollId);
        LinkCommand<PollDto> linkCommand =
                linkCreator.createLinkCommand(
                        linkReceiver,
                        pollResponseDto,
                        uriInfo);
        addLinks(linkCommand);
        return Response.ok()
                .entity(pollResponseDto)
                .type("application/json")
                .build();
    }
    @PATCH
    @Path("/{pollId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response update(PollDto pollRequestDto,
                           @PathParam("pollId") Long pollId,
                           @Context UriInfo uriInfo) {
        pollRequestDto.setPollId(pollId);
        PollDto pollResponseDto = pollService.update(pollRequestDto);
        LinkCommand<PollDto> linkCommand =
                linkCreator.createLinkCommand(
                        linkReceiver,
                        pollResponseDto,
                        uriInfo);
        addLinks(linkCommand);
        return Response.ok()
                .entity(pollResponseDto)
                .type("application/json")
                .build();
    }
    @DELETE
    @Path("/{pollId}")
    @Override
    public Response delete(@PathParam("pollId") Long pollId) {
        pollService.delete(pollId);
        return Response
                .noContent()
                .build();
    }
    @Inject
    public PollResourceImpl(@ProcessAs(ProcessType.POLL) @Default
                                ParentService<PollDto> pollService,
                            @ProcessAs(ProcessType.POLL) @Default
                            LinkCreator<PollDto> linkCreator,
                            LinkReceiver linkReceiver) {
        this.pollService = pollService;
        this.linkCreator = linkCreator;
        this.linkReceiver = linkReceiver;
    }
    protected PollResourceImpl() {}
}
