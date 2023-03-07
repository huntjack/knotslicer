package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = EventMemberDtoImpl.class)
public interface EventMemberDto {
    Long getEventId();
    void setEventId(Long eventId);
    Long getMemberId();
    void setMemberId(Long memberId);
}
