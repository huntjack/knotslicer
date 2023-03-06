package com.knotslicer.server.ports.interactor.datatransferobjects;

public interface EventMemberDto {
    Long getEventId();
    void setEventId(Long eventId);
    Long getMemberId();
    void setMemberId(Long memberId);
}
