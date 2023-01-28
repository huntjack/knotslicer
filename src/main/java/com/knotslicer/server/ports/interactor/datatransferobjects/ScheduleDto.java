package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;

@JsonDeserialize(as = ScheduleDtoImpl.class)
public interface ScheduleDto {
    Long getMemberId();
    void setMemberId(Long memberId);
    Long getScheduleId();
    void setScheduleId(Long scheduleId);
    LocalDateTime getStartTimeUtc();
    void setStartTimeUtc(LocalDateTime startTimeUtc);
    LocalDateTime getEndTimeUtc();
    void setEndTimeUtc(LocalDateTime endTimeUtc);
}
