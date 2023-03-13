package com.knotslicer.server.domain;


public interface PollAnswer {
    Long getPollAnswerId();
    void setPollAnswerId(Long pollAnswerId);
    Boolean isApproved();
    void setApproved(Boolean approved);
}
