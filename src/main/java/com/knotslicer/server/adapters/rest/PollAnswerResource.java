package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.PollAnswerDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface PollAnswerResource {
    Response create(@Valid PollAnswerDto pollAnswerRequestDto,
                    @Positive Long pollId,
                    UriInfo uriInfo);
    Response get(@Positive Long pollAnswerId,
                 @Positive Long pollId,
                 UriInfo uriInfo);
    Response getParentWithAllChildren(@Positive Long pollId,
                                      UriInfo uriInfo);
    Response update(@Valid PollAnswerDto pollAnswerDto,
                    @Positive Long pollAnswerId,
                    @Positive Long pollId,
                    UriInfo uriInfo);
    Response delete(@Positive Long pollAnswerId);
}
