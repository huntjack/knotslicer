package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface EventResource {
    Response create(EventDto eventDto, UriInfo uriInfo);
    Response get(Long eventId, UriInfo uriInfo);
    Response getWithPolls(Long eventId, UriInfo uriInfo);
    Response update(EventDto eventDto,
                    Long eventId,
                    UriInfo uriInfo);
    Response delete(Long eventId);
}
