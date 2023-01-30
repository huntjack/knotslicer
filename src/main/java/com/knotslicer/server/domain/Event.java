package com.knotslicer.server.domain;


public interface Event {
    Long getEventId();
    String getSubject();
    void setSubject(String subject);
    String getEventName();
    void setEventName(String eventName);
    String getEventDescription();
    void setEventDescription(String eventDescription);
}
