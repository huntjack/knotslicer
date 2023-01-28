package com.knotslicer.server.domain;


public interface User {
    Long getUserId();
    String getEmail();
    void setEmail(String email);
    String getUserName();
    void setUserName(String userName);
    String getUserDescription();
    void setUserDescription(String userDescription);
}
