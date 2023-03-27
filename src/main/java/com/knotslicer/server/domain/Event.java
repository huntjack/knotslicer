package com.knotslicer.server.domain;

public interface Event {
    Long getEventId();
    void setEventId(Long eventId);
    String getSubject();
    void setSubject(String subject);
    String getEventName();
    void setEventName(String eventName);
    String getEventDescription();
    void setEventDescription(String eventDescription);
}
