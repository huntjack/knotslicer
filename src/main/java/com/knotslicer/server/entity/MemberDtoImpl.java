package com.knotslicer.server.entity;


import java.io.Serializable;
import java.time.ZoneId;

public class MemberDtoImpl implements Member, Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String role;
    private String roleDescription;
    private ZoneId timeZone;

    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String getRole() {
        return role;
    }
    @Override
    public void setRole(String role) {
        this.role = role;
    }
    @Override
    public String getRoleDescription() {
        return roleDescription;
    }
    @Override
    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }
    @Override
    public ZoneId getTimeZone() {
        return timeZone;
    }
    @Override
    public void setTimeZone(ZoneId timeZone) {
        this.timeZone = timeZone;
    }
}
