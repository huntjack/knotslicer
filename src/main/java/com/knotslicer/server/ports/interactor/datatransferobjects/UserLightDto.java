package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.ZoneId;
import java.util.List;

@JsonDeserialize(as = UserLightDtoImpl.class)
public interface UserLightDto {
    void addLink(String url, String rel);
    Long getUserId();
    void setUserId(Long userId);
    public String getUserName();
    public void setUserName(String userName);
    public String getUserDescription();
    public void setUserDescription(String userDescription);
    ZoneId getTimeZone();
    void setTimeZone(ZoneId timeZone);
}
