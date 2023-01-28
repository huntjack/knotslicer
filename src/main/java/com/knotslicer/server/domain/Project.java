package com.knotslicer.server.domain;



public interface Project {
    Long getProjectId();
    String getProjectName();
    void setProjectName(String projectName);
    String getProjectDescription();
    void setProjectDescription(String projectDescription);
}
