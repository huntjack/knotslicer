package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(as = UserDtoImpl.class)
public interface UserDto {
    void addLink(String url, String rel);
    Long getUserId();
    void setUserId(Long userId);
    String getEmail();
    void setEmail(String email);
    String getUserName();
    void setUserName(String userName);
    String getUserDescription();
    void setUserDescription(String userDescription);
}
