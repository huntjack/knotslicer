package com.knotslicer.server.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.time.ZoneId;

@JsonDeserialize(as = MemberDtoImpl.class)
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
