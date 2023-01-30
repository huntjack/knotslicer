package com.knotslicer.server.ports.interactor.datatransferobjects;


import java.io.Serializable;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class MemberDtoImpl implements MemberDto, Serializable {
    private static final long serialVersionUID = 4000L;
    private Long userId;
    private Long memberId;
    private String name;
    private String role;
    private String roleDescription;
    private ZoneId timeZone;
    private List<Link> links = new ArrayList<>();
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
    public Long getMemberId() {return memberId;}
    @Override
    public void setMemberId(Long memberId) {this.memberId = memberId;}
    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String getRole() {
        return role;
    }
    @Override
    public void setRole(String role) {
        this.role = role;
    }
    @Override
    public String getRoleDescription() {
        return roleDescription;
    }
    @Override
    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }
    @Override
    public ZoneId getTimeZone() {
        return timeZone;
    }
    @Override
    public void setTimeZone(ZoneId timeZone) {
        this.timeZone = timeZone;
    }

    public List<Link> getLinks() {return links;}

    public void setLinks(List<Link> links) {this.links = links;}
}
