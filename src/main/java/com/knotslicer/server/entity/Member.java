package com.knotslicer.server.entity;

import java.time.ZoneId;

public interface Member {
    public String getName();
    public void setName(String name);
    public String getRole();
    public void setRole(String role);
    public String getRoleDescription();
    public void setRoleDescription(String roleDescription);
    public ZoneId getTimeZone();
    public void setTimeZone(ZoneId timeZone);
}
