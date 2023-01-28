package com.knotslicer.server.ports.interactor.datatransferobjects;


import java.io.Serializable;
import java.time.LocalDateTime;

public class ScheduleDtoImpl implements ScheduleDto, Serializable {
    private static final long serialVersionUID = 6000L;
    private Long memberId;
    private Long scheduleId;
    private LocalDateTime startTimeUtc;
    private LocalDateTime endTimeUtc;
    @Override
    public Long getMemberId() {return memberId;}
    @Override
    public void setMemberId(Long memberId) {this.memberId = memberId;}
    @Override
    public Long getScheduleId() {return scheduleId;}
    @Override
    public void setScheduleId(Long scheduleId) {this.scheduleId = scheduleId;}
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
