package com.knotslicer.server.entity;

import java.time.ZoneId;

public interface Member {
    String getName();
    void setName(String name);
    String getRole();
    void setRole(String role);
    String getRoleDescription();
    void setRoleDescription(String roleDescription);
    ZoneId getTimeZone();
    void setTimeZone(ZoneId timeZone);
}
