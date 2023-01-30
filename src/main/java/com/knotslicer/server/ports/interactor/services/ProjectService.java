package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;

public interface ProjectService {
    ProjectDto createProject(ProjectDto projectDto);
    ProjectDto getProject(Long projectId, Long userId);
    ProjectDto updateProject(ProjectDto projectDto);
    void deleteUser(Long projectId, Long userId);
}
