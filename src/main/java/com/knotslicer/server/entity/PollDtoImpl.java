package com.knotslicer.server.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PollDtoImpl implements Poll, Serializable {
    private static final long serialVersionUID = 1L;
    private LocalDateTime startTimeUtc;
    private LocalDateTime endTimeUtc;
    @Override
    public LocalDateTime getStartTimeUtc() {
        return startTimeUtc;
    }
    @Override
    public void setStartTimeUtc(LocalDateTime startTimeUtc) {
        this.startTimeUtc = startTimeUtc;
    }
    @Override
    public LocalDateTime getEndTimeUtc() {
        return endTimeUtc;
    }
    @Override
    public void setEndTimeUtc(LocalDateTime endTimeUtc) {
        this.endTimeUtc = endTimeUtc;
    }
}
