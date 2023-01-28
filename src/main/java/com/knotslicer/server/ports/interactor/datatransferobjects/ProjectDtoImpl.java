package com.knotslicer.server.ports.interactor.datatransferobjects;

import java.io.Serializable;

public class ProjectDtoImpl implements ProjectDto, Serializable {
    private static final long serialVersionUID = 3000L;
    private Long userId;
    private Long projectId;
    private String projectName;
    private String projectDescription;

    @Override
    public Long getUserId() {return userId;}
    @Override
    public void setUserId(Long userId) {this.userId = userId;}
    @Override
    public Long getProjectId() {return projectId;}
    @Override
    public void setProjectId(Long projectId) {this.projectId = projectId;}
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
