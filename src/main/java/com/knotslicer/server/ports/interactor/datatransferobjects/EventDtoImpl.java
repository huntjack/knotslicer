package com.knotslicer.server.ports.interactor.datatransferobjects;


import java.io.Serializable;

public class EventDtoImpl implements EventDto, Serializable {
    private static final long serialVersionUID = 5000L;
    private Long userId;
    private Long eventId;
    private String subject;
    private String eventName;
    private String eventDescription;
    @Override
    public Long getUserId() {return userId;}
    @Override
    public void setUserId(Long userId) {this.userId = userId;}
    @Override
    public Long getEventId() {return eventId;}
    @Override
    public void setEventId(Long eventId) {this.eventId = eventId;}
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
