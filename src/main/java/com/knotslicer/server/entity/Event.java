package com.knotslicer.server.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = EventDtoImpl.class)
public interface Event {
    String getSubject();
    void setSubject(String subject);
    String getEventName();
    void setEventName(String eventName);
    String getEventDescription();
    void setEventDescription(String eventDescription);
}
