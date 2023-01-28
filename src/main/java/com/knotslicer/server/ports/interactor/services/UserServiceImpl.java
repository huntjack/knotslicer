package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.ports.interactor.datatransferobjects.UserDto;
import com.knotslicer.server.ports.entitygateway.UserDao;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import com.knotslicer.server.domain.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class UserServiceImpl implements UserService {
    @Inject
    EntityDtoMapper entityDtoMapper;
    @Inject
    UserDao userDao;
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = entityDtoMapper.toEntity(userDto);
        User persistedUser = userDao.createUser(user);
        return entityDtoMapper.toDto(persistedUser);
    }

    @Override
    public UserDto getUser(Long userId) {
        Optional<User> optionalUser = userDao.getUser(userId);
        User user = optionalUser.orElseThrow(() -> new EntityNotFoundException("User not found."));
        return entityDtoMapper.toDto(user);
    }

    @Override
    public void deleteUser(Long userId) {
        userDao.deleteUser(userId);
    }
}
