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

@ProjectService
@ApplicationScoped
public class ProjectServiceImpl implements ParentService<ProjectDto> {
    private EntityDtoMapper entityDtoMapper;
    private ProjectDao projectDao;
    private MemberDao memberDao;

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
    public ProjectDto get(Long projectId) {
        Optional<Project> optionalProject = projectDao.get(projectId);
        Project project = unpackOptionalProject(optionalProject);
        Long userId = projectDao.getPrimaryParentId(projectId);
        return entityDtoMapper
                .toDto(project,
                        userId);
    }
    @Override
    public ProjectDto getWithChildren(Long projectId) {
        Optional<Project> optionalProject = memberDao.getProjectWithMembers(projectId);
        Project project = unpackOptionalProject(optionalProject);
        Long userId = projectDao.getPrimaryParentId(projectId);
        ProjectDto projectDto =
                entityDtoMapper.toDto(
                        project,
                        userId);
        return entityDtoMapper
                .addMemberDtosToProjectDto(
                        projectDto,
                        project);
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
        Long userId = projectDao.getPrimaryParentId(projectId);
        Project updatedProject = projectDao.update(projectToBeModified, userId);
        return entityDtoMapper.toDto(
                updatedProject,
                userId);
    }
    @Override
    public void delete(Long projectId) {
        Long userId = projectDao.getPrimaryParentId(projectId);
        projectDao.delete(projectId, userId);
    }
    @Inject
    public ProjectServiceImpl(EntityDtoMapper entityDtoMapper,
                              ProjectDao projectDao,
                              MemberDao memberDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.projectDao = projectDao;
        this.memberDao = memberDao;
    }
    protected ProjectServiceImpl() {}
}
