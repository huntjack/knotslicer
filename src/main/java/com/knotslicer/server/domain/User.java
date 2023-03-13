package com.knotslicer.server.domain;


import java.time.ZoneId;

public interface User {
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
