package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.adapters.rest.linkgenerator.Invoker;
import com.knotslicer.server.adapters.rest.linkgenerator.LinkReceiver;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcommands.LinkCommand;
import com.knotslicer.server.adapters.rest.linkgenerator.linkcreators.LinkCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.services.ParentService;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.net.URI;

@Path("/members")
@RequestScoped
public class MemberResourceImpl implements MemberResource {
    private ParentService<MemberDto> memberService;
    private LinkCreator<MemberDto> linkCreator;
    private LinkReceiver linkReceiver;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response create(MemberDto memberRequestDto,
                           @Context UriInfo uriInfo) {
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
    private URI addLinks(LinkCommand<MemberDto> linkCommand) {
        Invoker invoker =
                linkCreator.createInvoker(linkCommand);
        return invoker.executeCommand();
    }
    @GET
    @Path("/{memberId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response get(@PathParam("memberId") Long memberId,
                        @Context UriInfo uriInfo) {
        MemberDto memberResponseDto = memberService.get(memberId);
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
    @PATCH
    @Path("/{memberId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response update(MemberDto memberRequestDto,
                           @PathParam("memberId") Long memberId,
                           @Context UriInfo uriInfo) {
        memberRequestDto.setMemberId(memberId);
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
    public Response delete(@PathParam("memberId") Long memberId) {
        memberService.delete(memberId);
        return Response
                .noContent()
                .build();
    }
    @Inject
    public MemberResourceImpl(@ProcessAs(ProcessType.MEMBER)
                                  ParentService<MemberDto> memberService,
                              @ProcessAs(ProcessType.MEMBER) @Default
                              LinkCreator<MemberDto> linkCreator,
                              LinkReceiver linkReceiver) {
        this.memberService = memberService;
        this.linkCreator = linkCreator;
        this.linkReceiver = linkReceiver;
    }
    protected MemberResourceImpl(){}
}
