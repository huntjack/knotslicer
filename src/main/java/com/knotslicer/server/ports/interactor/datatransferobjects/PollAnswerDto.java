package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = PollAnswerDtoImpl.class)
public interface PollAnswerDto {

    Long getPollId();
    void setPollId(Long pollId);
    Long getPollAnswerId();
    void setPollAnswerId(Long pollAnswerId);
    Boolean isApproved();
    void setApproved(Boolean approved);
}
