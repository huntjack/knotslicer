package com.knotslicer.server.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class UserServiceImpl implements UserService {
    @PersistenceContext(unitName = "knotslicer_database")
    EntityManager entityManager;

    @Override
    public long createUser(UserDto userDto) {

    }
}
