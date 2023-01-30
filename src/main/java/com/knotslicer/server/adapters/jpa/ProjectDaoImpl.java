package com.knotslicer.server.adapters.jpa;

import com.knotslicer.server.domain.Project;
import com.knotslicer.server.domain.ProjectImpl;
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
        UserImpl userImpl = getUserWithProjects(userId);
        entityManager.detach(userImpl);
        userImpl.addProject((ProjectImpl) project);
        userImpl = entityManager.merge(userImpl);
        entityManager.flush();
        int projectIndex = userImpl
                .getProjects()
                .indexOf(project);
        return userImpl
                .getProjects()
                .get(projectIndex);
    }
    private UserImpl getUserWithProjects(Long userId) {
        TypedQuery<UserImpl> query = entityManager.createQuery
                        ("SELECT user FROM User user " +
                                "INNER JOIN FETCH user.projects " +
                                "WHERE user.userId = :userId", UserImpl.class)
                .setParameter("userId", userId);
        return query.getSingleResult();
    }
    @Override
    public Optional<Project> getProject(Long projectId) {
        Project project = entityManager.find(ProjectImpl.class, projectId);
        return Optional.ofNullable(project);
    }
    @Override
    public Project updateProject(Project inputProject, Long userId) {
        UserImpl userImpl = getUserWithProjects(userId);
        entityManager.detach(userImpl);
        int projectIndex = userImpl
                .getProjects()
                .indexOf(inputProject);
        Project projectToBeModified =
                userImpl
                .getProjects()
                .get(projectIndex);
        projectToBeModified
                .setProjectName(
                inputProject.getProjectName());
        projectToBeModified
                .setProjectDescription(
                inputProject
                .getProjectDescription());
        entityManager.merge(userImpl);
        entityManager.flush();
        return projectToBeModified;
    }
    @Override
    public void deleteProject(Long projectId, Long userId) {
        UserImpl userImpl = getUserWithProjects(userId);
        Project project = entityManager.find(ProjectImpl.class, projectId);
        userImpl.getProjects()
                .remove(project);
        entityManager.flush();
    }
}
