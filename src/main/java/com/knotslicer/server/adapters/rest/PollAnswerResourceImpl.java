package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.Invoker;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.WithChildren;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollAnswerDto;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.services.ParentService;
import com.knotslicer.server.ports.interactor.services.Service;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;

@Path("/polls/{pollId}/pollanswers")
@RequestScoped
public class PollAnswerResourceImpl implements PollAnswerResource {
    private Service<PollAnswerDto> pollAnswerService;
    private ParentService<PollDto> pollService;
    private LinkCreator<PollAnswerDto> linkCreator;
    private LinkCreator<PollDto> pollWithPollAnswersLinkCreator;
    private LinkReceiver linkReceiver;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response create(PollAnswerDto pollAnswerRequestDto,
                           @PathParam("pollId")Long pollId,
                           @Context UriInfo uriInfo) {
        pollAnswerRequestDto.setPollId(pollId);
        PollAnswerDto pollAnswerResponseDto = pollAnswerService.create(pollAnswerRequestDto);
        LinkCommand<PollAnswerDto> linkCommand = linkCreator
                .createLinkCommand(
                        linkReceiver,
                        pollAnswerResponseDto,
                        uriInfo);
        URI selfUri = addLinks(linkCommand);
        return Response.created(selfUri)
                .entity(pollAnswerResponseDto)
                .type("application/json")
                .build();
    }
    private URI addLinks(LinkCommand<?>  linkCommand) {
        Invoker invoker =
                linkCreator.createInvoker(linkCommand);
        return invoker.executeCommand();
    }
    @GET
    @Path("/{pollAnswerId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response get(@PathParam("pollAnswerId") Long pollAnswerId,
                        @PathParam("pollId")Long pollId,
                        @Context UriInfo uriInfo) {
        PollAnswerDto pollAnswerResponseDto = pollAnswerService.get(pollAnswerId);
        Long pollAnswerResponsePollId = pollAnswerResponseDto.getPollId();
        if(pollAnswerResponsePollId.equals(pollId)) {
            LinkCommand<PollAnswerDto> linkCommand = linkCreator
                    .createLinkCommand(
                            linkReceiver,
                            pollAnswerResponseDto,
                            uriInfo);
            addLinks(linkCommand);
            return Response.ok()
                    .entity(pollAnswerResponseDto)
                    .type("application/json")
                    .build();
        } else {
            throw new EntityNotFoundException("Sorry, poll answer not found.");
        }
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getParentWithAllChildren(@PathParam("pollId") Long pollId,
                                             @Context UriInfo uriInfo) {
        PollDto pollResponseDto = pollService.getWithChildren(pollId);
        LinkCommand<PollDto> linkCommand =
                pollWithPollAnswersLinkCreator.createLinkCommand(
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
    @Path("/{pollAnswerId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response update(PollAnswerDto pollRequestAnswerDto,
                           @PathParam("pollAnswerId")Long pollAnswerId,
                           @PathParam("pollId")Long pollId,
                           @Context UriInfo uriInfo) {
        pollRequestAnswerDto.setPollAnswerId(pollAnswerId);
        pollRequestAnswerDto.setPollId(pollId);
        PollAnswerDto pollAnswerResponseDto =
                pollAnswerService.update(pollRequestAnswerDto);
        LinkCommand<PollAnswerDto> linkCommand = linkCreator
                .createLinkCommand(
                        linkReceiver,
                        pollAnswerResponseDto,
                        uriInfo);
        addLinks(linkCommand);
        return Response.ok()
                .entity(pollAnswerResponseDto)
                .type("application/json")
                .build();
    }
    @DELETE
    @Path("/{pollAnswerId}")
    @Override
    public Response delete(@PathParam("pollAnswerId") Long pollAnswerId) {
        pollAnswerService.delete(pollAnswerId);
        return Response
                .noContent()
                .build();
    }
    @Inject
    public PollAnswerResourceImpl(@ProcessAs(ProcessType.POLLANSWER)
                                      Service<PollAnswerDto> pollAnswerService,
                                  @ProcessAs(ProcessType.POLL)
                                  ParentService<PollDto> pollService,
                                  @ProcessAs(ProcessType.POLLANSWER) @Default
                                  LinkCreator<PollAnswerDto> linkCreator,
                                  @ProcessAs(ProcessType.POLL) @WithChildren
                                      LinkCreator<PollDto> pollWithPollAnswersLinkCreator,
                                  LinkReceiver linkReceiver) {
        this.pollAnswerService = pollAnswerService;
        this.pollService = pollService;
        this.linkCreator = linkCreator;
        this.pollWithPollAnswersLinkCreator = pollWithPollAnswersLinkCreator;
        this.linkReceiver = linkReceiver;
    }
    protected PollAnswerResourceImpl() {}
}
