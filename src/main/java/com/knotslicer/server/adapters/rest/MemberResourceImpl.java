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

@Path("/users/{userId}/members")
@RequestScoped
public class MemberResourceImpl implements Resource<MemberDto> {
    @Inject
    @MemberService
    Service<MemberDto> memberService;
    @Inject
    @MemberLinkCreator
    LinkCreator<MemberDto> linkCreator;
    @Inject
    LinkReceiver linkReceiver;

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
        addLinks(linkCommand);
        URI uri = linkCommand.getSelfLink();
        return Response.created(uri)
                .entity(memberResponseDto)
                .type("application/json")
                .build();
    }
    private void addLinks(LinkCommand linkCommand) {
        Invoker invoker =
                linkCreator.createInvoker(linkCommand);
        invoker.executeCommand();
    }
    @GET
    @Path("/{memberId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response get(@PathParam("memberId") Long memberId,
                        @PathParam("userId") Long userId,
                        @Context UriInfo uriInfo) {
        MemberDto memberResponseDto = memberService.get(memberId, userId);
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
        memberService.delete(memberId, userId);
        return Response
                .noContent()
                .build();
    }
}
