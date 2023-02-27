package com.knotslicer.server.domain;

import java.time.ZoneId;

public interface Member {
    Long getMemberId();
    void setMemberId(Long memberId);
    String getName();
    void setName(String name);
    String getRole();
    void setRole(String role);
    String getRoleDescription();
    void setRoleDescription(String roleDescription);
}
