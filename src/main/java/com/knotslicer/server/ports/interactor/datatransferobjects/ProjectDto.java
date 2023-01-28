package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = ProjectDtoImpl.class)
public interface ProjectDto {
    Long getUserId();
    void setUserId(Long userId);
    Long getProjectId();
    void setProjectId(Long projectId);
    String getProjectName();
    void setProjectName(String projectName);
    String getProjectDescription();
    void setProjectDescription(String projectDescription);
}
