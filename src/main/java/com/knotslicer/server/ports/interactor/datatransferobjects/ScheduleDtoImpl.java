package com.knotslicer.server.ports.interactor.datatransferobjects;


import jakarta.validation.constraints.Future;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class ScheduleDtoImpl implements ScheduleDto, Serializable {
    private static final long serialVersionUID = 7000L;
    private Long scheduleId;
    private Long memberId;
    @Future
    private LocalDateTime startTimeUtc;
    @Future
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
    public Long getScheduleId() {return scheduleId;}
    @Override
    public void setScheduleId(Long scheduleId) {this.scheduleId = scheduleId;}
    @Override
    public Long getMemberId() {return memberId;}
    @Override
    public void setMemberId(Long memberId) {this.memberId = memberId;}
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
    @Override
    public List<Link> getLinks() {return links;}
}
