package com.knotslicer.server.adapters.jpa;

import com.knotslicer.server.domain.Project;
import com.knotslicer.server.domain.ProjectImpl;
import com.knotslicer.server.domain.User;
import com.knotslicer.server.domain.UserImpl;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ProcessAs(ProcessType.PROJECT)
@ApplicationScoped
@Transactional(rollbackOn={Exception.class})
public class ProjectDaoImpl implements ChildWithOneRequiredParentDao<Project, User> {
    @PersistenceContext(unitName = "knotslicer_database")
    private EntityManager entityManager;

    @Override
    public Project create(Project project, Long userId) {
        Optional<User> optionalUserWithProjects = getPrimaryParentWithChildren(userId);
        UserImpl userWithProjects = (UserImpl) optionalUserWithProjects
                .orElseThrow(() -> new EntityNotFoundException());
        entityManager.detach(userWithProjects);
        userWithProjects.addProject((ProjectImpl) project);
        userWithProjects = entityManager.merge(userWithProjects);
        entityManager.flush();
        Optional<Project> optionalProjectResponse = getProjectFromUser(
                userWithProjects,
                project);
        return optionalProjectResponse
                .orElseThrow(() -> new EntityNotFoundException());
    }
    @Override
    public Optional<User> getPrimaryParentWithChildren(Long userId) {
        TypedQuery<UserImpl> query = entityManager.createQuery
                        ("SELECT user FROM User user " +
                                "LEFT JOIN FETCH user.projects " +
                                "WHERE user.userId = :userId", UserImpl.class)
                .setParameter("userId", userId);
        User user = query.getSingleResult();
        return Optional.ofNullable(user);
    }
    private Optional<Project> getProjectFromUser(UserImpl userImpl, Project project) {
        List<ProjectImpl> projectImpls = userImpl.getProjects();
        int projectIndex = projectImpls.indexOf(project);
        project = projectImpls.get(projectIndex);
        return Optional.ofNullable(project);
    }
    @Override
    public Optional<Project> get(Long projectId) {
        Project project = entityManager.find(ProjectImpl.class, projectId);
        return Optional.ofNullable(project);
    }
    @Override
    public Optional<User> getPrimaryParent(Long projectId) {
        TypedQuery<UserImpl> query = entityManager.createQuery(
                "SELECT user FROM User user " +
                        "INNER JOIN user.projects project " +
                        "WHERE project.projectId = :projectId", UserImpl.class)
                .setParameter("projectId", projectId);
        User user = query.getSingleResult();
        return Optional.ofNullable(user);
    }
    @Override
    public Project update(Project projectInput, Long userId) {
        Optional<User> optionalUserWithProjects = getPrimaryParentWithChildren(userId);
        UserImpl userWithProjects = (UserImpl) optionalUserWithProjects
                .orElseThrow(() -> new EntityNotFoundException());
        Optional<Project> optionalProjectToBeModified =
                getProjectFromUser(userWithProjects, projectInput);
        Project projectToBeModified = optionalProjectToBeModified
                .orElseThrow(() -> new EntityNotFoundException());
        entityManager.detach(userWithProjects);
        projectToBeModified.setProjectName(
                projectInput.getProjectName());
        projectToBeModified.setProjectDescription(
                projectInput.getProjectDescription());
        userWithProjects = entityManager
                .merge(userWithProjects);
        entityManager.flush();
        Optional<Project> optionalProjectResponse = getProjectFromUser(
                userWithProjects,
                projectToBeModified);
        return optionalProjectResponse
                .orElseThrow(() -> new EntityNotFoundException());
    }
    @Override
    public void delete(Long projectId) {
        Optional<User> optionalUser = getPrimaryParent(projectId);
        User user = optionalUser
                .orElseThrow(() -> new EntityNotFoundException());
        Optional<User> optionalUserWithProjects =
                getPrimaryParentWithChildren(
                        user.getUserId());
        UserImpl userWithProjects = (UserImpl) optionalUserWithProjects
                .orElseThrow(() -> new EntityNotFoundException());
        Optional<Project> optionalProject = get(projectId);
        ProjectImpl projectImpl = (ProjectImpl) optionalProject
                .orElseThrow(() -> new EntityNotFoundException());
        userWithProjects.removeProject(projectImpl);
        entityManager.flush();
    }
}
