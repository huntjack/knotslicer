package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(as = PollAnswerDtoImpl.class)
public interface PollAnswerDto {
    void addLink(String url, String rel);
    Long getPollId();
    void setPollId(Long pollId);
    Long getPollAnswerId();
    void setPollAnswerId(Long pollAnswerId);
    Boolean isApproved();
    void setApproved(Boolean approved);
}