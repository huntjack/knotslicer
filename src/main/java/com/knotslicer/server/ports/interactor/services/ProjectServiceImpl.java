package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Project;
import com.knotslicer.server.ports.entitygateway.ProjectDao;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class ProjectServiceImpl implements ProjectService {
    @Inject
    EntityDtoMapper entityDtoMapper;
    @Inject
    ProjectDao projectDao;
    @Override
    public ProjectDto createProject(ProjectDto projectDto) {
        Project project = entityDtoMapper.toEntity(projectDto);
        project = projectDao.createProject(project, projectDto.getUserId());
        return entityDtoMapper.toDto(
                project,
                projectDto.getUserId());
    }
    @Override
    public ProjectDto getProject(Long projectId, Long userId) {
        Optional<Project> optionalProject = projectDao.getProject(projectId);
        Project project = unpackOptionalProject(optionalProject);
        return entityDtoMapper.toDto(project, userId);
    }
    private Project unpackOptionalProject(Optional<Project> optionalProject) {
        return optionalProject.orElseThrow(() -> new EntityNotFoundException("Project not found."));
    }
    @Override
    public ProjectDto updateProject(ProjectDto projectDto) {
        Optional<Project> optionalProject = projectDao.getProject(projectDto.getProjectId());
        Project projectToBeModified = unpackOptionalProject(optionalProject);
        Project inputProject = entityDtoMapper.toEntity(projectDto, projectToBeModified);
        Project updatedProject = projectDao.updateProject(inputProject, projectDto.getUserId());
        return entityDtoMapper.toDto(
                updatedProject,
                projectDto.getUserId());
    }
    @Override
    public void deleteUser(Long projectId, Long userId) {
        projectDao.deleteProject(projectId, userId);
    }
}
