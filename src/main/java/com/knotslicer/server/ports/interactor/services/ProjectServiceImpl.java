package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Project;
import com.knotslicer.server.ports.entitygateway.MemberDao;
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
    @Inject
    MemberDao memberDao;
    @Override
    public ProjectDto createProject(ProjectDto projectDto) {
        Project project = entityDtoMapper.toEntity(projectDto);
        project = projectDao.createProject(project, projectDto.getUserId());
        return entityDtoMapper.toDto(
                project,
                projectDto.getUserId());
    }
    @Override
    public ProjectDto getProject(Long projectId) {
        Optional<Project> optionalProject = projectDao.getProject(projectId);
        Project project = unpackOptionalProject(optionalProject);
        Long userId = projectDao.getUserId(projectId);
        return entityDtoMapper.toDto(project, userId);
    }
    @Override
    public ProjectDto getProjectWithMembers(Long projectId) {
        Optional<Project> optionalProject = memberDao.getProjectWithMembers(projectId);
        Project project = unpackOptionalProject(optionalProject);
        Long userId = projectDao.getUserId(projectId);
        ProjectDto projectDto = entityDtoMapper.toDto(project, userId);
        return entityDtoMapper.addMembers(projectDto, project);
    }
    private Project unpackOptionalProject(Optional<Project> optionalProject) {
        return optionalProject.orElseThrow(() -> new EntityNotFoundException("Project not found."));
    }
    @Override
    public ProjectDto updateProject(ProjectDto projectDto) {
        Optional<Project> optionalProject = projectDao.getProject(projectDto.getProjectId());
        Project projectToBeModified = unpackOptionalProject(optionalProject);
        projectToBeModified = entityDtoMapper.toEntity(projectDto, projectToBeModified);
        Project updatedProject = projectDao.updateProject(projectToBeModified);
        Long userId = projectDao.getUserId(projectDto.getProjectId());
        return entityDtoMapper.toDto(
                updatedProject,
                userId);
    }
    @Override
    public void deleteUser(Long projectId) {
        projectDao.deleteProject(projectId);
    }
}
