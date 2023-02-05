package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.ZoneId;
import java.util.List;

@JsonDeserialize(as = UserDtoImpl.class)
public interface UserDto extends Linkable {
    Long getUserId();
    void setUserId(Long userId);
    String getEmail();
    void setEmail(String email);
    String getUserName();
    void setUserName(String userName);
    String getUserDescription();
    void setUserDescription(String userDescription);
    ZoneId getTimeZone();
    void setTimeZone(ZoneId timeZone);
}
