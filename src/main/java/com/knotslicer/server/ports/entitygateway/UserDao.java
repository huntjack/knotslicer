package com.knotslicer.server.ports.entitygateway;

import com.knotslicer.server.domain.User;
import java.util.Optional;

public interface UserDao {
    User createUser(User user);
    Optional<User> getUser(Long userId);
    Optional<User> getUserWithProjects(Long userId);
    Optional<User> getUserWithEvents(Long userId);
    Optional<User> getUserWithMembers(Long userId);
    User updateUser(User inputUser);
    void deleteUser(Long userId);
}
