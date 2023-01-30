package com.knotslicer.server.domain;


public interface PollAnswer {
    Long getPollAnswerId();
    Boolean isApproved();
    void setApproved(Boolean approved);
}
