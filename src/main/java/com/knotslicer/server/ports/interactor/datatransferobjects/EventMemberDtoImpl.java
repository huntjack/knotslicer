package com.knotslicer.server.ports.interactor.datatransferobjects;

import jakarta.validation.constraints.Positive;

import java.io.Serializable;

public class EventMemberDtoImpl implements EventMemberDto, Serializable {
    private static final long serialVersionUID = 10000L;
    private Long eventId;
    @Positive
    private Long memberId;

    @Override
    public Long getEventId() {return eventId;}
    @Override
    public void setEventId(Long eventId) {this.eventId = eventId;}
    @Override
    public Long getMemberId() {return memberId;}
    @Override
    public void setMemberId(Long memberId) {this.memberId = memberId;}

}
