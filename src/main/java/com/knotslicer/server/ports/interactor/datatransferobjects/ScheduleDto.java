package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.LocalDateTime;
import java.util.List;

@JsonDeserialize(as = ScheduleDtoImpl.class)
public interface ScheduleDto extends Linkable {
    Long getScheduleId();
    void setScheduleId(Long scheduleId);
    Long getMemberId();
    void setMemberId(Long memberId);
    Long getUserId();
    public void setUserId(Long userId);
    LocalDateTime getStartTimeUtc();
    void setStartTimeUtc(LocalDateTime startTimeUtc);
    LocalDateTime getEndTimeUtc();
    void setEndTimeUtc(LocalDateTime endTimeUtc);
}
