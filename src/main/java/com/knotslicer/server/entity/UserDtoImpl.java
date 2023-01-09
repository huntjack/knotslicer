package com.knotslicer.server.entity;

import java.io.Serializable;

public class UserDtoImpl implements User, Serializable {

    private static final long serialVersionUID = 1L;
    private String email;
    private String userName;
    private String userDescription;
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
}
