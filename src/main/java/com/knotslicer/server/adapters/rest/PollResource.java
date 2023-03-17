package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.PollDto;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface PollResource {
    Response create(@Valid PollDto pollDto, UriInfo uriInfo);
    Response get(Long pollId, UriInfo uriInfo);
    Response update(@Valid PollDto pollDto,
                    Long pollId,
                    UriInfo uriInfo);
    Response delete(Long pollId);
}
