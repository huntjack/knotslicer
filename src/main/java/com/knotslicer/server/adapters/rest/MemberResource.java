package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface MemberResource {
    Response create(MemberDto memberRequestDto,
                    Long userId,
                    UriInfo uriInfo);
    Response get(Long memberId,
                 Long userId,
                 UriInfo uriInfo);
    Response update(MemberDto memberRequestDto,
                    Long memberId,
                    Long userId,
                    UriInfo uriInfo);
    Response delete( Long memberId, Long userId);
}
