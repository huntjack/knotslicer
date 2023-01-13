package com.knotslicer.server.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserDtoImpl.class)
public interface User {
    String getEmail();
    void setEmail(String email);
    String getUserName();
    void setUserName(String userName);
    String getUserDescription();
    void setUserDescription(String userDescription);
}
