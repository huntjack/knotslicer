package com.knotslicer.server.entity;

public interface Event {
    public String getSubject();
    public void setSubject(String subject);
    public String getEventName();
    public void setEventName(String eventName);
    public String getEventDescription();
    public void setEventDescription(String eventDescription);
}
