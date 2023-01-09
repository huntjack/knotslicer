package com.knotslicer.server.entity;

import java.io.Serializable;

public class ProjectDtoImpl implements Project, Serializable {
    private static final long serialVersionUID = 1L;
    private String projectName;
    private String projectDescription;

    @Override
    public String getProjectName() {
        return projectName;
    }
    @Override
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    @Override
    public String getProjectDescription() {
        return projectDescription;
    }

    @Override
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }
}
