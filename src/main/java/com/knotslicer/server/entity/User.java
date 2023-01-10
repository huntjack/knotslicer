package com.knotslicer.server.entity;

public interface User {
    String getEmail();
    void setEmail(String email);
    String getUserName();
    void setUserName(String userName);
    String getUserDescription();
    void setUserDescription(String userDescription);
}
