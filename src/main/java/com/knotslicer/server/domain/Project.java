package com.knotslicer.server.domain;


public interface Project {
    Long getProjectId();
    void setProjectId(Long projectId);
    String getProjectName();
    void setProjectName(String projectName);
    String getProjectDescription();
    void setProjectDescription(String projectDescription);
}
