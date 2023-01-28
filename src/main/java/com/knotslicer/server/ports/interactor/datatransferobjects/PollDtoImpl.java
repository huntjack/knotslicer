package com.knotslicer.server.ports.interactor.datatransferobjects;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PollDtoImpl implements PollDto, Serializable {
    private static final long serialVersionUID = 7000L;
    private Long eventId;
    private Long pollId;
    private LocalDateTime startTimeUtc;
    private LocalDateTime endTimeUtc;
    @Override
    public Long getEventId() {return eventId;}
    @Override
    public void setEventId(Long eventId) {this.eventId = eventId;}
    @Override
    public Long getPollId() {return pollId;}
    @Override
    public void setPollId(Long pollId) {this.pollId = pollId;}
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
