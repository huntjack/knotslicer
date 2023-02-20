package com.knotslicer.server.ports.entitygateway;

import com.knotslicer.server.domain.User;
import com.knotslicer.server.domain.UserImpl;

import java.util.Optional;

public interface UserDao {
    User create(User user);
    Optional<User> get(Long id);
    User update(User user);
    public void delete(Long id);
}
