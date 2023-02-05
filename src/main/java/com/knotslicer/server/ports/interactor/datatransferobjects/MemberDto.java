package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = MemberDtoImpl.class)
public interface MemberDto extends Linkable {
    Long getUserId();
    void setUserId(Long userId);
    Long getProjectId();
    void setProjectId(Long projectId);
    Long getMemberId();
    void setMemberId(Long memberId);
    String getName();
    void setName(String name);
    String getRole();
    void setRole(String role);
    String getRoleDescription();
    void setRoleDescription(String roleDescription);
}
