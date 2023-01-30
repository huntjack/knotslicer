package com.knotslicer.server.domain;

import java.time.LocalDateTime;

public interface Schedule {
    Long getScheduleId();
    LocalDateTime getStartTimeUtc();
    void setStartTimeUtc(LocalDateTime startTimeUtc);
    LocalDateTime getEndTimeUtc();
    void setEndTimeUtc(LocalDateTime endTimeUtc);
}
