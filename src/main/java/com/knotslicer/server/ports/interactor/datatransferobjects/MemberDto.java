package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.ZoneId;
import java.util.List;

@JsonDeserialize(as = MemberDtoImpl.class)
public interface MemberDto {
    void addLink(String url, String rel);
    Long getUserId();
    void setUserId(Long userId);
    Long getMemberId();
    void setMemberId(Long memberId);
    String getName();
    void setName(String name);
    String getRole();
    void setRole(String role);
    String getRoleDescription();
    void setRoleDescription(String roleDescription);
    ZoneId getTimeZone();
    void setTimeZone(ZoneId timeZone);
}
