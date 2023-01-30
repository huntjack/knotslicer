package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(as = EventDtoImpl.class)
public interface EventDto {
    void addLink(String url, String rel);
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
