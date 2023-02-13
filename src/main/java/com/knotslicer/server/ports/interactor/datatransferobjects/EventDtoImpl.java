package com.knotslicer.server.ports.interactor.datatransferobjects;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class EventDtoImpl implements EventDto, Serializable {
    private static final long serialVersionUID = 6000L;
    private Long userId;
    private Long eventId;
    private String subject;
    private String eventName;
    private String eventDescription;
    private List<Link> links = new LinkedList<>();
    @Override
    public void addLink(String url, String rel) {
        Link link = createLink();
        link.setLink(url);
        link.setRel(rel);
        links.add(link);
    }
    private Link createLink() {
        return new LinkImpl();
    }
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
    public List<Link> getLinks() {return links;}
    public void setLinks(List<Link> links) {this.links = links;}
}
