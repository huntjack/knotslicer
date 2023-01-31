package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.LinkedList;

@JsonDeserialize(as = ProjectDtoImpl.class)
public interface ProjectDto {
    void addLink(String url, String rel);
    Long getUserId();
    void setUserId(Long userId);
    Long getProjectId();
    void setProjectId(Long projectId);
    String getProjectName();
    void setProjectName(String projectName);
    String getProjectDescription();
    void setProjectDescription(String projectDescription);
    LinkedList<MemberDto> getMembers();
    void setMembers(LinkedList<MemberDto> members);
}
