package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.ScheduleDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface ScheduleResource {
    Response create(ScheduleDto scheduleDto,
                    Long memberId,
                    UriInfo uriInfo);
    Response get(Long scheduleId,
                 Long memberId,
                 UriInfo uriInfo);
    Response getParentWithAllChildren(Long memberId,
                                      UriInfo uriInfo);
    Response update(ScheduleDto scheduleDto,
                    Long scheduleId,
                    Long memberId,
                   UriInfo uriInfo);
    Response delete(Long scheduleId);
}
