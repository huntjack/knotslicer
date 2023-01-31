package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import com.knotslicer.server.ports.interactor.services.MemberService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

@Path("/users/{userId}/members")
@RequestScoped
public class MemberResourceImpl implements MemberResource {
    @Inject
    MemberService memberService;
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response createMember(MemberDto memberRequestDto, @PathParam("userId") Long userId, @Context UriInfo uriInfo) {
        memberRequestDto.setUserId(userId);
        MemberDto memberResponseDto = memberService.createMember(memberRequestDto);
        return null;
    }
    @GET
    @Path("/{memberId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response getMember(@PathParam("memberId") Long memberId, @PathParam("userId") Long userId, @Context UriInfo uriInfo) {
        return null;
    }
    @PUT
    @Path("/{memberId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response updateMember(MemberDto memberRequestDto, @PathParam("userId") Long userId, @Context UriInfo uriInfo) {
        return null;
    }
    @DELETE
    @Path("/{memberId}")
    @Override
    public Response deleteProject(@PathParam("memberId") Long memberId, @PathParam("userId") Long userId) {
        return null;
    }
}
