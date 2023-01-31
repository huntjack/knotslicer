package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;

public interface ProjectService {
    ProjectDto createProject(ProjectDto projectDto);
    ProjectDto getProject(Long projectId);
    ProjectDto getProjectWithMembers(Long projectId);
    ProjectDto updateProject(ProjectDto projectDto);
    void deleteUser(Long projectId);
}
