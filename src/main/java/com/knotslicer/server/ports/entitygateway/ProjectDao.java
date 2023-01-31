package com.knotslicer.server.ports.entitygateway;

import com.knotslicer.server.domain.Project;

import java.util.Optional;

public interface ProjectDao {
    Project createProject(Project project, Long userId);
    Optional<Project> getProject(Long projectId);
    Project updateProject(Project inputProject);
    Long getUserId(Long projectId);
    void deleteProject(Long projectId);
}
