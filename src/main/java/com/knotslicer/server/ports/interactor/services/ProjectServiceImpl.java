package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Project;
import com.knotslicer.server.domain.User;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.entitygateway.MemberDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ProcessAs(ProcessType.PROJECT)
@ApplicationScoped
public class ProjectServiceImpl implements ParentService<ProjectDto> {
    private EntityDtoMapper entityDtoMapper;
    private ChildWithOneRequiredParentDao<Project, User> projectDao;
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
        User user = projectDao.getPrimaryParent(projectId);
        Long userId = user.getUserId();
        return entityDtoMapper
                .toDto(project, userId);
    }
    @Override
    public ProjectDto getWithChildren(Long projectId) {
        Optional<Project> optionalProject = memberDao.getSecondaryParentWithChildren(projectId);
        Project project = unpackOptionalProject(optionalProject);
        User user = projectDao.getPrimaryParent(projectId);
        Long userId = user.getUserId();
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
        projectToBeModified = entityDtoMapper
                .toEntity(projectDto, projectToBeModified);
        User user = projectDao
                .getPrimaryParent(projectId);
        Long userId = user.getUserId();
        Project updatedProject = projectDao
                .update(projectToBeModified, userId);
        return entityDtoMapper.toDto(
                updatedProject,
                userId);
    }
    @Override
    public void delete(Long projectId) {
        projectDao.delete(projectId);
    }
    @Inject
    public ProjectServiceImpl(EntityDtoMapper entityDtoMapper,
                              @ProcessAs(ProcessType.PROJECT)
                              ChildWithOneRequiredParentDao<Project, User> projectDao,
                              MemberDao memberDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.projectDao = projectDao;
        this.memberDao = memberDao;
    }
    protected ProjectServiceImpl() {}
}
