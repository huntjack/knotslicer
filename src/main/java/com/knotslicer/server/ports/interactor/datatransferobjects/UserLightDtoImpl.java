package com.knotslicer.server.ports.interactor.datatransferobjects;

import java.io.Serializable;

public class UserLightDtoImpl implements UserLightDto, Serializable {
    private static final long serialVersionUID = 2000L;
    private Long userId;
    private String userName;
    private String userDescription;

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
}
