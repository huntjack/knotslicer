package com.knotslicer.server.entity;

public class UserLightDtoImpl implements UserLight {
    private String userName;
    private String userDescription;
    public String getUserName() {return userName;}
    public void setUserName(String userName) {this.userName = userName;}
    public String getUserDescription() {return userDescription;}
    public void setUserDescription(String userDescription) {this.userDescription = userDescription;}
}
