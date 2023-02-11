package com.knotslicer.server.adapters.jpa;

import com.knotslicer.server.domain.User;
import com.knotslicer.server.domain.UserImpl;
import com.knotslicer.server.ports.entitygateway.UserDao;
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
    public User create(User user) {
        entityManager.persist(user);
        entityManager.flush();
        entityManager.refresh(user);
        return user;
    }
    @Override
    public Optional<User> get(Long userId) {
        User user = entityManager.find(UserImpl.class, userId);
        return Optional.ofNullable(user);
    }
    @Override
    public User update(User user) {
        User userToBeModified = entityManager.find(UserImpl.class, user.getUserId());
        userToBeModified.setUserName(user.getUserName());
        userToBeModified.setUserDescription(user.getUserDescription());
        userToBeModified.setTimeZone(user.getTimeZone());
        entityManager.flush();
        return userToBeModified;
    }
    @Override
    public void delete(Long userId) {
        User user = entityManager.find(UserImpl.class, userId);
        entityManager.remove(user);
        entityManager.flush();
    }
}
