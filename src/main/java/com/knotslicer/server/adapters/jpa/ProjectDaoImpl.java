package com.knotslicer.server.adapters.jpa;

import com.knotslicer.server.domain.Project;
import com.knotslicer.server.domain.ProjectImpl;
import com.knotslicer.server.domain.User;
import com.knotslicer.server.domain.UserImpl;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
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
        UserImpl userImpl = getUserWithProjectsFromJpa(userId);
        entityManager.detach(userImpl);
        userImpl.addProject((ProjectImpl) project);
        userImpl = entityManager.merge(userImpl);
        entityManager.flush();
        return getProjectFromUser(
                userImpl,
                project);
    }
    private UserImpl getUserWithProjectsFromJpa(Long userId) {
        TypedQuery<UserImpl> query = entityManager.createQuery
                        ("SELECT user FROM User user " +
                                "LEFT JOIN FETCH user.projects " +
                                "WHERE user.userId = :userId", UserImpl.class)
                .setParameter("userId", userId);
        return query.getSingleResult();
    }
    private Project getProjectFromUser(UserImpl userImpl, Project project) {
        List<ProjectImpl> projectImpls = userImpl.getProjects();
        int projectIndex = projectImpls.indexOf(project);
        return projectImpls.get(projectIndex);
    }
    @Override
    public Optional<Project> get(Long projectId) {
        Project project = entityManager.find(ProjectImpl.class, projectId);
        return Optional.ofNullable(project);
    }
    @Override
    public Optional<User> getPrimaryParentWithChildren(Long userId) {
        User user = getUserWithProjectsFromJpa(userId);
        return Optional.ofNullable(user);
    }
    @Override
    public Long getPrimaryParentId(Long projectId) {
        TypedQuery<UserImpl> query = entityManager.createQuery
                        ("SELECT user FROM User user " +
                                "INNER JOIN user.projects project " +
                                "WHERE project.projectId = :projectId", UserImpl.class)
                .setParameter("projectId", projectId);
        User user = query.getSingleResult();
        return user.getUserId();
    }
    @Override
    public Project update(Project projectInput, Long userId) {
        UserImpl userImpl = getUserWithProjectsFromJpa(userId);
        entityManager.detach(userImpl);
        Project projectToBeModified =
                getProjectFromUser(userImpl, projectInput);
        projectToBeModified.setProjectName(
                projectInput.getProjectName());
        projectToBeModified.setProjectDescription(
                projectInput.getProjectDescription());
        userImpl = entityManager
                .merge(userImpl);
        entityManager.flush();
        return getProjectFromUser(
                userImpl,
                projectToBeModified);
    }
    @Override
    public void delete(Long projectId, Long userId) {
        UserImpl userImpl = getUserWithProjectsFromJpa(userId);
        ProjectImpl projectImpl = entityManager.find(ProjectImpl.class, projectId);
        userImpl.removeProject(projectImpl);
        entityManager.flush();
    }
}
