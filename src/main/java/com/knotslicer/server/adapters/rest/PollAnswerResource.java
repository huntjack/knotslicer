package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.PollAnswerDto;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface PollAnswerResource {
    Response create(@Valid PollAnswerDto pollAnswerRequestDto,
                    Long pollId,
                    UriInfo uriInfo);
    Response get(Long pollAnswerId,
                 Long pollId,
                 UriInfo uriInfo);
    Response getParentWithAllChildren(Long pollId,
                                      UriInfo uriInfo);
    Response update(@Valid PollAnswerDto pollAnswerDto,
                    Long pollAnswerId,
                    Long pollId,
                    UriInfo uriInfo);
    Response delete(Long pollAnswerId);
}
