package com.knotslicer.server.adapters.jpa;

import com.knotslicer.server.domain.User;
import com.knotslicer.server.domain.UserImpl;
import com.knotslicer.server.ports.entitygateway.UserDao;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
@Transactional(rollbackOn={Exception.class})
public class UserDaoImpl implements UserDao {
    @PersistenceContext(unitName = "knotslicer_database")
    EntityManager entityManager;
    @Override
    public User createUser(User user) {
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }
    @Override
    public Optional<User> getUser(Long userId) {
        User user = entityManager.find(UserImpl.class, userId);
        return Optional.ofNullable(user);
    }
    @Override
    public Optional<User> getUserWithProjects(Long userId) {
        return Optional.empty();
    }
    @Override
    public Optional<User> getUserWithEvents(Long userId) {
        return Optional.empty();
    }
    @Override
    public Optional<User> getUserWithMembers(Long userId) {
        return Optional.empty();
    }
    @Override
    public User updateUser(User user) {
        return null;
    }
    @Override
    public void deleteUser(Long userId) {
        User user = entityManager.find(UserImpl.class, userId);
        entityManager.remove(user);
        entityManager.flush();
    }
}
