package com.knotslicer.server.entity;

import java.time.LocalDateTime;

public interface Poll {
    public LocalDateTime getStartTimeUtc();
    public void setStartTimeUtc(LocalDateTime startTimeUtc);
    public LocalDateTime getEndTimeUtc();
    public void setEndTimeUtc(LocalDateTime endTimeUtc);
}
