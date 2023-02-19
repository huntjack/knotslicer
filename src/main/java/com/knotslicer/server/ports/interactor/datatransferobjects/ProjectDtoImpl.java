package com.knotslicer.server.ports.interactor.datatransferobjects;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class ProjectDtoImpl implements ProjectDto, Serializable {
    private static final long serialVersionUID = 3000L;
    private Long userId;
    private Long projectId;
    private String projectName;
    private String projectDescription;
    private List<MemberDto> members = new LinkedList<>();
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
    public List<MemberDto> getMembers() {return members;}
    @Override
    public void setMembers(List<MemberDto> members) {this.members = members;}
    public List<Link> getLinks() {return links;}
    public void setLinks(List<Link> links) {this.links = links;}
}
