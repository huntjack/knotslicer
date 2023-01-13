package com.knotslicer.server.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

@JsonDeserialize(as = PollDtoImpl.class)
public interface Poll {
    LocalDateTime getStartTimeUtc();
    void setStartTimeUtc(LocalDateTime startTimeUtc);
    LocalDateTime getEndTimeUtc();
    void setEndTimeUtc(LocalDateTime endTimeUtc);
}
