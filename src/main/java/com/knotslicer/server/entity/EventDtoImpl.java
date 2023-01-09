package com.knotslicer.server.entity;


import java.io.Serializable;

public class EventDtoImpl implements Event, Serializable {
    private static final long serialVersionUID = 1L;
    private String subject;
    private String eventName;
    private String eventDescription;
    @Override
    public String getSubject() {
        return subject;
    }
    @Override
    public void setSubject(String subject) {
        this.subject = subject;
    }
    @Override
    public String getEventName() {
        return eventName;
    }
    @Override
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    @Override
    public String getEventDescription() {
        return eventDescription;
    }
    @Override
    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }
}
