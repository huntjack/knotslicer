package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.LocalDateTime;

@JsonDeserialize(as = ScheduleDtoImpl.class)
public interface ScheduleDto extends Linkable {
    Long getScheduleId();
    void setScheduleId(Long scheduleId);
    Long getMemberId();
    void setMemberId(Long memberId);
    LocalDateTime getStartTimeUtc();
    void setStartTimeUtc(LocalDateTime startTimeUtc);
    LocalDateTime getEndTimeUtc();
    void setEndTimeUtc(LocalDateTime endTimeUtc);
}
