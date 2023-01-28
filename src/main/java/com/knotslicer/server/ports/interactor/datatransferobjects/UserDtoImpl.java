package com.knotslicer.server.ports.interactor.datatransferobjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class UserDtoImpl implements UserDto, Serializable {
    private static final long serialVersionUID = 1000L;
    private Long userId;
    private String email;
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
    @Override
    public Link createLink() {
        return new Link();
    }

    @Override
    public Long getUserId() {return userId;}
    @Override
    public void setUserId(Long userId) {this.userId = userId;}
    @Override
    public String getEmail() {
        return email;
    }
    @Override
    public void setEmail(String email) {
        this.email = email;
    }
    @Override
    public String getUserName() {
        return userName;
    }
    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }
    @Override
    public String getUserDescription() {
        return userDescription;
    }
    @Override
    public void setUserDescription(String userDescription) {
        this.userDescription = userDescription;
    }
    @Override
    public List<Link> getLinks() {return links;}
    @Override
    public void setLinks(List<Link> links) {this.links = links;}
}
