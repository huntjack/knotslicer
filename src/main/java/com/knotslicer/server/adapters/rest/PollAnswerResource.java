package com.knotslicer.server.adapters.rest;

import com.knotslicer.server.ports.interactor.datatransferobjects.PollAnswerDto;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

public interface PollAnswerResource {
    Response create(PollAnswerDto pollAnswerRequestDto,
                    Long pollId,
                    UriInfo uriInfo);
    Response get(Long pollAnswerId,
                 Long pollId,
                 UriInfo uriInfo);
    Response getParentWithAllChildren(Long pollId,
                                      UriInfo uriInfo);
    Response update(PollAnswerDto pollAnswerDto,
                    Long pollAnswerId,
                    Long pollId,
                    UriInfo uriInfo);
    Response delete(Long pollAnswerId);
}
