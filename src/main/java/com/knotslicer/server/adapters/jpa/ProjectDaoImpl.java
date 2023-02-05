package com.knotslicer.server.adapters.jpa;

import com.knotslicer.server.domain.Project;
import com.knotslicer.server.domain.ProjectImpl;
import com.knotslicer.server.domain.User;
import com.knotslicer.server.domain.UserImpl;
import com.knotslicer.server.ports.entitygateway.ProjectDao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
@Transactional(rollbackOn={Exception.class})
public class ProjectDaoImpl implements ProjectDao {
    @PersistenceContext(unitName = "knotslicer_database")
    EntityManager entityManager;
    @Override
    public Project createProject(Project project, Long userId) {
        UserImpl userImpl = getUserWithProjectsFromJpa(userId);
        entityManager.detach(userImpl);
        userImpl.addProject((ProjectImpl) project);
        userImpl = entityManager.merge(userImpl);
        entityManager.flush();
        project = getProjectFromUser(userImpl, project);
        entityManager.refresh(project);
        return project;
    }
    private UserImpl getUserWithProjectsFromJpa(Long userId) {
        TypedQuery<UserImpl> query = entityManager.createQuery
                        ("SELECT user FROM User user " +
                                "INNER JOIN FETCH user.projects " +
                                "WHERE user.userId = :userId", UserImpl.class)
                .setParameter("userId", userId);
        return query.getSingleResult();
    }
    private ProjectImpl getProjectFromUser(UserImpl userImpl, Project project) {
        int projectIndex =
                userImpl
                .getProjects()
                .indexOf(project);
        return userImpl
                .getProjects()
                .get(projectIndex);
    }
    @Override
    public Optional<Project> getProject(Long projectId) {
        Project project = entityManager.find(ProjectImpl.class, projectId);
        return Optional.ofNullable(project);
    }
    @Override
    public Project updateProject(Project inputProject) {
        Long userId = getUserId(inputProject.getProjectId());
        UserImpl userImpl = getUserWithProjectsFromJpa(userId);
        entityManager.detach(userImpl);
        Project projectToBeModified =
                getProjectFromUser(userImpl, inputProject);
        projectToBeModified
                .setProjectName(
                inputProject.getProjectName());
        projectToBeModified
                .setProjectDescription(
                inputProject.getProjectDescription());
        userImpl = entityManager.merge(userImpl);
        entityManager.flush();
        projectToBeModified = getProjectFromUser(userImpl, inputProject);
        return projectToBeModified;
    }
    @Override
    public Long getUserId(Long projectId) {
        TypedQuery<UserImpl> query = entityManager.createQuery
                        ("SELECT user FROM User user " +
                                "INNER JOIN user.projects p " +
                                "WHERE p.projectId = :projectId", UserImpl.class)
                .setParameter("projectId", projectId);
        User user = query.getSingleResult();
        return user.getUserId();
    }
    @Override
    public void deleteProject(Long projectId) {
        Long userId = getUserId(projectId);
        UserImpl userImpl = getUserWithProjectsFromJpa(userId);
        Project project = entityManager.find(ProjectImpl.class, projectId);
        userImpl.getProjects()
                .remove(project);
        entityManager.flush();
    }
}
