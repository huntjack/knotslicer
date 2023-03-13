package com.knotslicer.server.adapters.jpa;

import com.knotslicer.server.domain.User;
import com.knotslicer.server.domain.UserImpl;
import com.knotslicer.server.ports.entitygateway.UserDao;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
@Transactional(rollbackOn={Exception.class})
public class UserDaoImpl implements UserDao {
    @PersistenceContext(unitName = "knotslicer_database")
    private EntityManager entityManager;

    @Override
    public User create(User user) {
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }
    @Override
    public Optional<User> get(Long userId) {
        User user = entityManager.find(UserImpl.class, userId);
        return Optional.ofNullable(user);
    }
    @Override
    public User update(User userInput) {
        Long userId = userInput.getUserId();
        User userToBeModified = getUser(userId);
        userToBeModified.setUserName(
                userInput.getUserName());
        userToBeModified.setUserDescription(
                userInput.getUserDescription());
        userToBeModified.setTimeZone(
                userInput.getTimeZone());
        entityManager.flush();
        return userToBeModified;
    }
    private User getUser(Long userId) {
        Optional<User> optionalUserToBeModified = get(userId);
        return optionalUserToBeModified
                .orElseThrow(() -> new EntityNotFoundException());
    }
    @Override
    public void delete(Long userId) {
        User user = getUser(userId);
        entityManager.remove(user);
        entityManager.flush();
    }
}
