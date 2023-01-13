package com.knotslicer.server.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = PollAnswerDtoImpl.class)
public interface PollAnswer {
    Boolean isApproved();
    void setApproved(Boolean approved);
}
