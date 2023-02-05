package com.knotslicer.server.ports.interactor.datatransferobjects;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class PollDtoImpl implements PollDto, Serializable {
    private static final long serialVersionUID = 7000L;
    private Long eventId;
    private Long pollId;
    private LocalDateTime startTimeUtc;
    private LocalDateTime endTimeUtc;
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
    public Long getEventId() {return eventId;}
    @Override
    public void setEventId(Long eventId) {this.eventId = eventId;}
    @Override
    public Long getPollId() {return pollId;}
    @Override
    public void setPollId(Long pollId) {this.pollId = pollId;}
    @Override
    public LocalDateTime getStartTimeUtc() {
        return startTimeUtc;
    }
    @Override
    public void setStartTimeUtc(LocalDateTime startTimeUtc) {
        this.startTimeUtc = startTimeUtc;
    }
    @Override
    public LocalDateTime getEndTimeUtc() {
        return endTimeUtc;
    }
    @Override
    public void setEndTimeUtc(LocalDateTime endTimeUtc) {
        this.endTimeUtc = endTimeUtc;
    }
    public List<Link> getLinks() {return links;}
    public void setLinks(List<Link> links) {this.links = links;}
}
