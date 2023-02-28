package com.knotslicer.server.ports.interactor.datatransferobjects;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;

public class UserLightDtoImpl implements UserLightDto, Serializable {
    private static final long serialVersionUID = 2000L;
    private Long userId;
    private String userName;
    private String userDescription;
    private ZoneId timeZone;
    private List<ProjectDto> projects;
    private List<MemberDto> members;
    private List<EventDto> events;
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
    public String getUserName() {return userName;}
    @Override
    public void setUserName(String userName) {this.userName = userName;}
    @Override
    public String getUserDescription() {return userDescription;}
    @Override
    public void setUserDescription(String userDescription) {this.userDescription = userDescription;}
    @Override
    public ZoneId getTimeZone() {return timeZone;}
    @Override
    public void setTimeZone(ZoneId timeZone) {this.timeZone = timeZone;}
    @Override
    public List<ProjectDto> getProjects() {return projects;}
    @Override
    public void setProjects(List<ProjectDto> projects) {this.projects = projects;}
    @Override
    public List<MemberDto> getMembers() {return members;}
    @Override
    public void setMembers(List<MemberDto> members) {this.members = members;}
    @Override
    public List<EventDto> getEvents() {return events;}
    @Override
    public void setEvents(List<EventDto> events) {this.events = events;}
    @Override
    public List<Link> getLinks() {return links;}
    @Override
    public void setLinks(List<Link> links) {this.links = links;}
}
