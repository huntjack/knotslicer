package com.knotslicer.server.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = Project.class)
public interface Project {
    String getProjectName();
    void setProjectName(String projectName);
    String getProjectDescription();
    void setProjectDescription(String projectDescription);
}
