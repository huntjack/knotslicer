package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Project;
import com.knotslicer.server.domain.User;
import com.knotslicer.server.ports.entitygateway.ChildWithOneParentDao;
import com.knotslicer.server.ports.entitygateway.MemberDao;
import com.knotslicer.server.ports.entitygateway.ProjectDao;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Map;
import java.util.Optional;

@ProjectService
@ApplicationScoped
public class ProjectServiceImpl implements Service<ProjectDto> {
    @Inject
    EntityDtoMapper entityDtoMapper;
    @Inject
    ProjectDao projectDao;
    @Inject
    MemberDao memberDao;

    @Override
    public ProjectDto create(ProjectDto projectDto) {
        Project project = entityDtoMapper.toEntity(projectDto);
        Long userId = projectDto.getUserId();
        project = projectDao.create(project, userId);
        return entityDtoMapper.toDto(
                project,
                userId);
    }
    @Override
    public ProjectDto get(Map<String,Long> primaryKeys) {
        Long projectId = primaryKeys.get("projectId");
        Optional<Project> optionalProject = projectDao.get(projectId);
        Project project = unpackOptionalProject(optionalProject);
        Long userId = primaryKeys.get("userId");
        return entityDtoMapper.toDto(project, userId);
    }
    @Override
    public ProjectDto getWithChildren(Map<String,Long> primaryKeys) {
        Long projectId = primaryKeys.get("projectId");
        Optional<Project> optionalProject = memberDao.getProjectWithMembers(projectId);
        Project project = unpackOptionalProject(optionalProject);
        Long userId = primaryKeys.get("userId");
        ProjectDto projectDto = entityDtoMapper.toDto(project, userId);
        return entityDtoMapper.addMemberDtosToProjectDto(projectDto, project);
    }
    private Project unpackOptionalProject(Optional<Project> optionalProject) {
        return optionalProject.orElseThrow(() -> new EntityNotFoundException("Project not found."));
    }
    @Override
    public ProjectDto update(ProjectDto projectDto) {
        Long projectId = projectDto.getProjectId();
        Optional<Project> optionalProject =
                projectDao.get(projectId);
        Project projectToBeModified = unpackOptionalProject(optionalProject);
        projectToBeModified = entityDtoMapper.toEntity(projectDto, projectToBeModified);
        Long userId = projectDto.getUserId();
        Project updatedProject = projectDao.update(projectToBeModified, userId);
        return entityDtoMapper.toDto(
                updatedProject,
                userId);
    }
    @Override
    public void delete(Map<String,Long> primaryKeys) {
        Long projectId = primaryKeys.get("projectId");
        Long userId = primaryKeys.get("userId");
        projectDao.delete(projectId, userId);
    }
}
