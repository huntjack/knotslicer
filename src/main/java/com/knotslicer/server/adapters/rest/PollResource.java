package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.PollDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface PollResource {
    Response create(PollDto pollDto, UriInfo uriInfo);
    Response get(Long pollId, UriInfo uriInfo);
    Response update(PollDto pollDto,
                    Long pollId,
                    UriInfo uriInfo);
    Response delete(Long pollId);
}
