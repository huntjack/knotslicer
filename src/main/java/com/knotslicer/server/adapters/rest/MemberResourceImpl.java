package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.Invoker;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.MemberLinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.MemberWithSchedulesLinkCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.services.MemberService;
import com.knotslicer.server.ports.interactor.services.ParentService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Path("/users/{userId}/members")
@RequestScoped
public class MemberResourceImpl implements MemberResource {
    @Inject
    @MemberService
    private ParentService<MemberDto> memberService;
    @Inject
    @MemberLinkCreator
    private LinkCreator<MemberDto> linkCreator;
    @Inject
    private LinkReceiver linkReceiver;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response create(MemberDto memberRequestDto,
                           @PathParam("userId") Long userId,
                           @Context UriInfo uriInfo) {
        memberRequestDto.setUserId(userId);
        MemberDto memberResponseDto = memberService.create(memberRequestDto);
        LinkCommand<MemberDto> linkCommand =
                linkCreator.createLinkCommand(
                        linkReceiver,
                        memberResponseDto,
                        uriInfo);
        URI selfUri = addLinks(linkCommand);
        return Response.created(selfUri)
                .entity(memberResponseDto)
                .type("application/json")
                .build();
    }
    private URI addLinks(LinkCommand linkCommand) {
        Invoker invoker =
                linkCreator.createInvoker(linkCommand);
        return invoker.executeCommand();
    }
    @GET
    @Path("/{memberId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response get(@PathParam("memberId") Long memberId,
                        @PathParam("userId") Long userId,
                        @Context UriInfo uriInfo) {
        Map<String,Long> ids = packIds(memberId, userId);
        MemberDto memberResponseDto = memberService.get(ids);
        LinkCommand<MemberDto> linkCommand =
                linkCreator.createLinkCommand(
                        linkReceiver,
                        memberResponseDto,
                        uriInfo);
        addLinks(linkCommand);
        return Response.ok()
                .entity(memberResponseDto)
                .type("application/json")
                .build();
    }
    private Map<String,Long> packIds(Long memberId, Long userId) {
        Map<String,Long> ids = new HashMap<>();
        ids.put("memberId", memberId);
        ids.put("userId", userId);
        return ids;
    }
    @PUT
    @Path("/{memberId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response update(MemberDto memberRequestDto,
                           @PathParam("memberId") Long memberId,
                           @PathParam("userId") Long userId,
                           @Context UriInfo uriInfo) {
        memberRequestDto.setMemberId(memberId);
        memberRequestDto.setUserId(userId);
        MemberDto memberResponseDto =
                memberService.update(memberRequestDto);
        LinkCommand<MemberDto> linkCommand =
                linkCreator.createLinkCommand(
                                linkReceiver,
                                memberResponseDto,
                                uriInfo);
        addLinks(linkCommand);
        return Response.ok()
                .entity(memberResponseDto)
                .type("application/json")
                .build();
    }
    @DELETE
    @Path("/{memberId}")
    @Override
    public Response delete(@PathParam("memberId") Long memberId,
                           @PathParam("userId") Long userId) {
        Map<String,Long> ids = packIds(memberId, userId);
        memberService.delete(ids);
        return Response
                .noContent()
                .build();
    }
}
