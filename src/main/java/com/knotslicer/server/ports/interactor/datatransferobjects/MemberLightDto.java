package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = MemberLightDtoImpl.class)
public interface MemberLightDto extends Linkable {
    Long getUserId();
    void setUserId(Long userId);
    Long getMemberId();
    void setMemberId(Long memberId);
    Long getProjectId();
    void setProjectId(Long projectId);
    String getName();
    void setName(String name);
    String getRole();
    void setRole(String role);
    String getRoleDescription();
    void setRoleDescription(String roleDescription);
}
