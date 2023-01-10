package com.knotslicer.server.control;

import com.knotslicer.server.entity.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserServiceImpl implements UserService {
    @PersistenceContext(unitName = "knotslicer_database")
    EntityManager entityManager;
    @Inject
    EntityMapper entityMapper;
    @Override
    @Transactional(rollbackOn={Exception.class})
    public long createUser(User userDto) {

    }
}
