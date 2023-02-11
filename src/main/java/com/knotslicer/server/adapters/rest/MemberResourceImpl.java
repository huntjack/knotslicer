package com.knotslicer.server.adapters.rest;

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
public class MemberResourceImpl implements Resource<MemberDto> {
    @Inject
    @MemberService
    Service<MemberDto> memberService;
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response create(MemberDto memberRequestDto, @PathParam("userId") Long userId, @Context UriInfo uriInfo) {
        memberRequestDto.setUserId(userId);
        MemberDto memberResponseDto = memberService.create(memberRequestDto);
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
    public Response get(@PathParam("memberId") Long memberId, @PathParam("userId") Long userId, @Context UriInfo uriInfo) {
        return null;
    }
    @GET
    @Path("/{memberId}/schedules")
    @Override
    public Response getWithChildren(@PathParam("memberId") Long memberId, @PathParam("userId") Long userId, @Context UriInfo uriInfo) {
        return null;
    }

    @PUT
    @Path("/{memberId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public Response update(MemberDto memberRequestDto, @PathParam("memberId") Long memberId, @PathParam("userId") Long userId, @Context UriInfo uriInfo) {
        return null;
    }
    @DELETE
    @Path("/{memberId}")
    @Override
    public Response delete(@PathParam("memberId") Long memberId, @PathParam("userId") Long userId) {
        memberService.delete(memberId, userId);
        return Response.noContent()
                .build();
    }
}
