package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.services.MemberService;
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
    MemberService memberService;
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response createMember(MemberDto memberRequestDto, @PathParam("userId") Long userId, @Context UriInfo uriInfo) {
        memberRequestDto.setUserId(userId);
        MemberDto memberResponseDto = memberService.createMember(memberRequestDto);
        addLinks(uriInfo, memberResponseDto);
        URI selfUri = getUriForSelf(uriInfo, memberResponseDto);
        return Response.created(selfUri)
                .entity(memberResponseDto)
                .type("application/json")
                .build();
    }
    private void addLinks(UriInfo uriInfo, MemberDto memberResponseDto) {
        URI selfUri = getUriForSelf(uriInfo,
                memberResponseDto);
        URI userUri = getUriForUser(uriInfo,
                memberResponseDto);
        memberResponseDto.addLink(selfUri.toString(), "self");
        memberResponseDto.addLink(userUri.toString(), "user");
    }
    private URI getUriForSelf(UriInfo uriInfo, MemberDto memberResponseDto) {
        String baseUri = uriInfo
                .getBaseUriBuilder()
                .path(UserResourceImpl.class)
                .build()
                .toString();
        String secondHalfOfUri = "/{userId}/members/{memberId}";
        String template = baseUri + secondHalfOfUri;
        Map<String, Long> parameters = new HashMap<>(3);
        parameters.put(
                "userId",
                memberResponseDto.getUserId());
        parameters.put(
                "memberId",
                memberResponseDto.getMemberId());
        UriBuilder uriBuilder = UriBuilder.fromPath(template);
        return uriBuilder
                .buildFromMap(parameters);
    }
    private URI getUriForUser(UriInfo uriInfo, MemberDto memberResponseDto) {
        return uriInfo
                .getBaseUriBuilder()
                .path(UserResourceImpl.class)
                .path(Long.toString(memberResponseDto.getUserId()))
                .build();
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
