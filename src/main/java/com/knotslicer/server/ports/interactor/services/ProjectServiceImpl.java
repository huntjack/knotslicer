package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Project;
import com.knotslicer.server.domain.User;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.ProjectDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import java.util.Optional;

@ProcessAs(ProcessType.PROJECT)
@Default
@ApplicationScoped
public class ProjectServiceImpl implements ParentService<ProjectDto> {
    private EntityDtoMapper entityDtoMapper;
    private ChildWithOneRequiredParentDao<Project, User> projectDao;
    private ChildWithTwoParentsDao<Member, User, Project> memberDao;

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
        Project project = getProject(projectId);
        Long userId = getUserId(projectId);
        return entityDtoMapper
                .toDto(project, userId);
    }
    private Project getProject(Long projectId) {
        Optional<Project> optionalProject = projectDao.get(projectId);
        return optionalProject
                .orElseThrow(() -> new EntityNotFoundException());
    }
    private Long getUserId(Long projectId) {
        Optional<User> optionalUser = projectDao.getPrimaryParent(projectId);
        User user = optionalUser
                .orElseThrow(() -> new EntityNotFoundException());
        return user.getUserId();
    }
    @Override
    public ProjectDto getWithChildren(Long projectId) {
        Optional<Project> optionalProject = memberDao.getSecondaryParentWithChildren(projectId);
        Project project = optionalProject
                .orElseThrow(() -> new EntityNotFoundException());
        Long userId = getUserId(projectId);
        ProjectDto projectDto =
                entityDtoMapper.toDto(
                        project,
                        userId);
        return entityDtoMapper
                .addMemberDtosToProjectDto(
                        projectDto,
                        project);
    }
    @Override
    public ProjectDto update(ProjectDto projectDto) {
        Long projectId = projectDto.getProjectId();
        Project projectToBeModified = getProject(projectId);
        projectToBeModified = entityDtoMapper
                .toEntity(projectDto, projectToBeModified);
        Long userId = getUserId(projectId);
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
                              @ProcessAs(ProcessType.MEMBER)
                              ChildWithTwoParentsDao<Member, User, Project> memberDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.projectDao = projectDao;
        this.memberDao = memberDao;
    }
    protected ProjectServiceImpl() {}
}
