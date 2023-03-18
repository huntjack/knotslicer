package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface MemberResource {
    Response create(@Valid MemberDto memberRequestDto, UriInfo uriInfo);
    Response get(@Positive Long memberId, UriInfo uriInfo);
    Response getWithEvents(@Positive Long memberId, UriInfo uriInfo);
    Response update(@Valid MemberDto memberRequestDto,
                    @Positive Long memberId,
                    UriInfo uriInfo);
    Response delete(@Positive Long memberId);
}
