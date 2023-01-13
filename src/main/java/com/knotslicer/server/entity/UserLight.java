package com.knotslicer.server.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserLightDtoImpl.class)
public interface UserLight {
    public String getUserName();
    public void setUserName(String userName);
    public String getUserDescription();
    public void setUserDescription(String userDescription);
}
