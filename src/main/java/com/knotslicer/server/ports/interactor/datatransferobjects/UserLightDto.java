package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = UserLightDtoImpl.class)
public interface UserLightDto {
    Long getUserId();
    void setUserId(Long userId);
    public String getUserName();
    public void setUserName(String userName);
    public String getUserDescription();
    public void setUserDescription(String userDescription);
}
