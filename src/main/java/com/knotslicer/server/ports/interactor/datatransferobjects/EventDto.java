package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = EventDtoImpl.class)
public interface EventDto {
    Long getUserId();
    void setUserId(Long userId);
    Long getEventId();
    void setEventId(Long eventId);
    String getSubject();
    void setSubject(String subject);
    String getEventName();
    void setEventName(String eventName);
    String getEventDescription();
    void setEventDescription(String eventDescription);
}
