package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.Invoker;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.MemberWithSchedulesLinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.ScheduleLinkCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.ScheduleDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.services.*;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;

@Path("/members/{memberId}/schedules")
@RequestScoped
public class ScheduleResourceImpl implements ScheduleResource {
    private Service<ScheduleDto> scheduleService;
    private ParentService<MemberDto> memberService;
    private LinkCreator<ScheduleDto> linkCreator;
    private LinkCreator<MemberDto> memberWithSchedulesLinkCreator;
    private LinkReceiver linkReceiver;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response create(ScheduleDto scheduleRequestDto,
                           @PathParam("memberId")Long memberId,
                           @Context UriInfo uriInfo) {
        scheduleRequestDto.setMemberId(memberId);
        ScheduleDto scheduleResponseDto = scheduleService.create(scheduleRequestDto);
        LinkCommand<ScheduleDto> linkCommand = linkCreator
                .createLinkCommand(
                        linkReceiver,
                        scheduleResponseDto,
                        uriInfo);
        URI selfUri = addLinks(linkCommand);
        return Response.created(selfUri)
                .entity(scheduleResponseDto)
                .type("application/json")
                .build();
    }
    private URI addLinks(LinkCommand<?>  linkCommand) {
        Invoker invoker =
                linkCreator.createInvoker(linkCommand);
        return invoker.executeCommand();
    }
    @GET
    @Path("/{scheduleId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response get(@PathParam("scheduleId") Long scheduleId,
                        @PathParam("memberId")Long memberId,
                        @Context UriInfo uriInfo) {
        ScheduleDto scheduleResponseDto = scheduleService.get(scheduleId);
        Long scheduleResponseMemberId = scheduleResponseDto.getMemberId();
        if(scheduleResponseMemberId.equals(memberId)) {
            LinkCommand<ScheduleDto> linkCommand = linkCreator
                    .createLinkCommand(
                            linkReceiver,
                            scheduleResponseDto,
                            uriInfo);
            addLinks(linkCommand);
            return Response.ok()
                    .entity(scheduleResponseDto)
                    .type("application/json")
                    .build();
        } else {
            throw new EntityNotFoundException("Sorry, schedule not found.");
        }
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getParentWithAllChildren(@PathParam("memberId") Long memberId,
                                             @Context UriInfo uriInfo) {
        MemberDto memberResponseDto = memberService.getWithChildren(memberId);
        LinkCommand<MemberDto> linkCommand =
                memberWithSchedulesLinkCreator.createLinkCommand(
                        linkReceiver,
                        memberResponseDto,
                        uriInfo);
        addLinks(linkCommand);
        return Response.ok()
                .entity(memberResponseDto)
                .type("application/json")
                .build();
    }
    @PUT
    @Path("/{scheduleId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response update(ScheduleDto scheduleDto,
                           @PathParam("scheduleId")Long scheduleId,
                           @PathParam("memberId")Long memberId,
                           @Context UriInfo uriInfo) {
        scheduleDto.setScheduleId(scheduleId);
        scheduleDto.setMemberId(memberId);
        ScheduleDto scheduleResponseDto =
                scheduleService.update(scheduleDto);
        LinkCommand<ScheduleDto> linkCommand = linkCreator
                .createLinkCommand(
                        linkReceiver,
                        scheduleResponseDto,
                        uriInfo);
        addLinks(linkCommand);
        return Response.ok()
                .entity(scheduleResponseDto)
                .type("application/json")
                .build();
    }
    @DELETE
    @Path("/{scheduleId}")
    @Override
    public Response delete(@PathParam("scheduleId") Long scheduleId) {
        scheduleService.delete(scheduleId);
        return Response
                .noContent()
                .build();
    }
    @Inject
    public ScheduleResourceImpl(@ScheduleService Service<ScheduleDto> scheduleService,
                                @MemberService ParentService<MemberDto> memberService,
                                @ScheduleLinkCreator LinkCreator<ScheduleDto> linkCreator,
                                @MemberWithSchedulesLinkCreator LinkCreator<MemberDto> memberWithSchedulesLinkCreator,
                                LinkReceiver linkReceiver) {
        this.scheduleService = scheduleService;
        this.memberService = memberService;
        this.linkCreator = linkCreator;
        this.memberWithSchedulesLinkCreator = memberWithSchedulesLinkCreator;
        this.linkReceiver = linkReceiver;
    }
    protected ScheduleResourceImpl() {}
}
