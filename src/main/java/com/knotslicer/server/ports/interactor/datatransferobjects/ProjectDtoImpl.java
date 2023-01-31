package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.knotslicer.server.domain.Member;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ProjectDtoImpl implements ProjectDto, Serializable {
    private static final long serialVersionUID = 3000L;
    private Long userId;
    private Long projectId;
    private String projectName;
    private String projectDescription;
    private LinkedList<MemberDto> members = new LinkedList<>();
    private LinkedList<Link> links = new LinkedList<>();
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
    public Long getProjectId() {return projectId;}
    @Override
    public void setProjectId(Long projectId) {this.projectId = projectId;}
    @Override
    public String getProjectName() {
        return projectName;
    }
    @Override
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    @Override
    public String getProjectDescription() {
        return projectDescription;
    }
    @Override
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }
    @Override
    public LinkedList<MemberDto> getMembers() {return members;}
    @Override
    public void setMembers(LinkedList<MemberDto> members) {this.members = members;}
    public LinkedList<Link> getLinks() {return links;}
    public void setLinks(LinkedList<Link> links) {this.links = links;}
}
