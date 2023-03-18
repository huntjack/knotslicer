package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.PollDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface PollResource {
    Response create(@Valid PollDto pollDto, UriInfo uriInfo);
    Response get(@Positive Long pollId, UriInfo uriInfo);
    Response update(@Valid PollDto pollDto,
                    @Positive Long pollId,
                    UriInfo uriInfo);
    Response delete(@Positive Long pollId);
}
