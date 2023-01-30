package com.knotslicer.server.ports.interactor.datatransferobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserLightDtoImpl implements UserLightDto, Serializable {
    private static final long serialVersionUID = 2000L;
    private Long userId;
    private String userName;
    private String userDescription;
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
    public String getUserName() {return userName;}
    @Override
    public void setUserName(String userName) {this.userName = userName;}
    @Override
    public String getUserDescription() {return userDescription;}
    @Override
    public void setUserDescription(String userDescription) {this.userDescription = userDescription;}
    public List<Link> getLinks() {return links;}
    public void setLinks(List<Link> links) {this.links = links;}
}
