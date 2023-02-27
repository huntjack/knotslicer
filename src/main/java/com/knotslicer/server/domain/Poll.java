package com.knotslicer.server.domain;

import java.time.LocalDateTime;

public interface Poll {
    Long getPollId();
    void setPollId(Long pollId);
    LocalDateTime getStartTimeUtc();
    void setStartTimeUtc(LocalDateTime startTimeUtc);
    LocalDateTime getEndTimeUtc();
    void setEndTimeUtc(LocalDateTime endTimeUtc);
}
