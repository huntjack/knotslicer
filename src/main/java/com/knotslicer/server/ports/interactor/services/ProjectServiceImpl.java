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
import java.util.Optional;

@ApplicationScoped
@ProjectService
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
        project = projectDao.create(project, projectDto.getUserId());
        return entityDtoMapper.toDto(
                project,
                projectDto.getUserId());
    }
    @Override
    public ProjectDto get(Long projectId, Long userId) {
        Optional<Project> optionalProject = projectDao.get(projectId);
        Project project = unpackOptionalProject(optionalProject);
        return entityDtoMapper.toDto(project, userId);
    }
    @Override
    public ProjectDto getWithChildren(Long projectId, Long userId) {
        Optional<Project> optionalProject = memberDao.getProjectWithMembers(projectId);
        Project project = unpackOptionalProject(optionalProject);
        ProjectDto projectDto = entityDtoMapper.toDto(project, userId);
        return entityDtoMapper.addMembers(projectDto, project);
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
    public void delete(Long projectId, Long userId) {
        projectDao.delete(projectId, userId);
    }
}
