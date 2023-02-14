package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.Invoker;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.MemberLinkCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.services.MemberService;
import com.knotslicer.server.ports.interactor.services.Service;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Path("/users/{userId}/members")
@RequestScoped
public class MemberResourceImpl implements ParentResource<MemberDto> {
    @Inject
    @MemberService
    private Service<MemberDto> memberService;
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
        LinkCommand linkCommand = linkCreator
                .createLinkCommand(
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
        Map<String,Long> primaryKeys = packPrimaryKeys(memberId, userId);
        MemberDto memberResponseDto = memberService.get(primaryKeys);
        LinkCommand linkCommand = linkCreator
                .createLinkCommand(
                        linkReceiver,
                        memberResponseDto,
                        uriInfo);
        addLinks(linkCommand);
        return Response.ok()
                .entity(memberResponseDto)
                .type("application/json")
                .build();
    }
    private Map<String,Long> packPrimaryKeys(Long memberId, Long userId) {
        Map<String,Long> primaryKeys = new HashMap<>(3);
        primaryKeys
                .put("memberId", memberId);
        primaryKeys
                .put("userId", userId);
        return primaryKeys;
    }
    @GET
    @Path("/{memberId}/schedules")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getWithChildren(@PathParam("memberId") Long memberId,
                                    @PathParam("userId") Long userId,
                                    @Context UriInfo uriInfo) {
        return null;
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
        LinkCommand linkCommand = linkCreator
                .createLinkCommand(
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
        Map<String,Long> primaryKeys = packPrimaryKeys(memberId, userId);
        memberService.delete(primaryKeys);
        return Response
                .noContent()
                .build();
    }
}
