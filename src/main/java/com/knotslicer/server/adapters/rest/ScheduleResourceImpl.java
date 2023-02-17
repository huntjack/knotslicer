package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.Invoker;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.MemberWithSchedulesLinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.ScheduleLinkCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.ScheduleDto;
import com.knotslicer.server.ports.interactor.services.MemberService;
import com.knotslicer.server.ports.interactor.services.ParentService;
import com.knotslicer.server.ports.interactor.services.ScheduleService;
import com.knotslicer.server.ports.interactor.services.Service;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Path("/users/{userId}/members/{memberId}/schedules")
@RequestScoped
public class ScheduleResourceImpl implements ScheduleResource {
    @Inject
    @ScheduleService
    private Service<ScheduleDto> scheduleService;
    @Inject
    @MemberService
    private ParentService<MemberDto> memberService;
    @Inject
    @ScheduleLinkCreator
    LinkCreator<ScheduleDto> linkCreator;
    @Inject
    @MemberWithSchedulesLinkCreator
    private LinkCreator<MemberDto> memberWithSchedulesLinkCreator;
    @Inject
    private LinkReceiver linkReceiver;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response create(ScheduleDto scheduleRequestDto,
                           @PathParam("memberId")Long memberId,
                           @PathParam("userId")Long userId,
                           @Context UriInfo uriInfo) {
        scheduleRequestDto.setMemberId(memberId);
        scheduleRequestDto.setUserId(userId);
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
    private URI addLinks(LinkCommand linkCommand) {
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
                        @PathParam("userId")Long userId,
                        @Context UriInfo uriInfo) {
        Map<String,Long> ids = packIds(scheduleId, memberId, userId);
        ScheduleDto scheduleResponseDto = scheduleService.get(ids);
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
    private Map<String,Long> packIds(Long scheduleId, Long memberId, Long userId) {
        Map<String,Long> ids = new HashMap<>();
        ids.put("scheduleId", scheduleId);
        ids.put("memberId", memberId);
        ids.put("userId", userId);
        return ids;
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getParentWithAllChildren(@PathParam("memberId") Long memberId,
                                             @PathParam("userId") Long userId,
                                             @Context UriInfo uriInfo) {
        Map<String,Long> ids = packMemberIdAndUserId(memberId, userId);
        MemberDto memberResponseDto = memberService.getWithChildren(ids);
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
    private Map<String,Long> packMemberIdAndUserId(Long memberId, Long userId) {
        Map<String,Long> ids = new HashMap<>();
        ids.put("memberId", memberId);
        ids.put("userId", userId);
        return ids;
    }
    @PUT
    @Path("/{scheduleId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response update(ScheduleDto scheduleDto,
                           @PathParam("scheduleId")Long scheduleId,
                           @PathParam("memberId")Long memberId,
                           @PathParam("userId")Long userId,
                           @Context UriInfo uriInfo) {
        scheduleDto.setScheduleId(scheduleId);
        scheduleDto.setMemberId(memberId);
        scheduleDto.setUserId(userId);
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
    public Response delete(@PathParam("scheduleId") Long scheduleId,
                           @PathParam("memberId")Long memberId) {
        Map<String,Long> ids = packIds(scheduleId, memberId);
        scheduleService.delete(ids);
        return Response
                .noContent()
                .build();
    }
    private Map<String,Long> packIds(Long scheduleId, Long memberId) {
        Map<String,Long> ids = new HashMap<>();
        ids.put("scheduleId", scheduleId);
        ids.put("memberId", memberId);
        return ids;
    }
}
