package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.ScheduleDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface ScheduleResource {
    Response create(@Valid ScheduleDto scheduleDto,
                    @Positive Long memberId,
                    UriInfo uriInfo);
    Response get(@Positive Long scheduleId,
                 @Positive Long memberId,
                 UriInfo uriInfo);
    Response getParentWithAllChildren(@Positive Long memberId, UriInfo uriInfo);
    Response update(@Valid ScheduleDto scheduleDto,
                    @Positive Long scheduleId,
                    @Positive Long memberId,
                   UriInfo uriInfo);
    Response delete(@Positive Long scheduleId);
}
