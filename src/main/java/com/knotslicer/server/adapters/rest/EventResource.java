package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface EventResource {
    Response create(@Valid EventDto eventDto, UriInfo uriInfo);
    Response get(@Positive Long eventId, UriInfo uriInfo);
    Response getWithChildren(@Positive Long eventId, UriInfo uriInfo);
    Response findAvailableEventTimes(@Positive Long eventId, UriInfo uriInfo);
    Response update(@Valid EventDto eventDto,
                    @Positive Long eventId,
                    UriInfo uriInfo);
    Response delete(@Positive Long eventId);
}
