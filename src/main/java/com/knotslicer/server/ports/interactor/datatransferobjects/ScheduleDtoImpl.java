package com.knotslicer.server.ports.interactor.datatransferobjects;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class ScheduleDtoImpl implements ScheduleDto, Serializable {
    private static final long serialVersionUID = 6000L;
    private Long memberId;
    private Long scheduleId;
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
    public Long getMemberId() {return memberId;}
    @Override
    public void setMemberId(Long memberId) {this.memberId = memberId;}
    @Override
    public Long getScheduleId() {return scheduleId;}
    @Override
    public void setScheduleId(Long scheduleId) {this.scheduleId = scheduleId;}
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
