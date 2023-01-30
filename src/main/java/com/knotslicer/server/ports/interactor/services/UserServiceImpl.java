package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.ports.interactor.datatransferobjects.UserDto;
import com.knotslicer.server.ports.entitygateway.UserDao;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
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
        user = userDao.createUser(user);
        return entityDtoMapper.toDto(user);
    }
    @Override
    public UserLightDto getUser(Long userId) {
        Optional<User> optionalUser = userDao.getUser(userId);
        User user = unpackOptionalUser(optionalUser);
        return entityDtoMapper.toLightDto(user);
    }
    private User unpackOptionalUser(Optional<User> optionalUser) {
        return optionalUser.orElseThrow(() -> new EntityNotFoundException("User not found."));
    }
    @Override
    public UserLightDto updateUser(UserLightDto userLightDto) {
        Optional<User> optionalUser = userDao.getUser(userLightDto.getUserId());
        User userToBeModified = unpackOptionalUser(optionalUser);
        User modifiedUser = entityDtoMapper.toEntity(userLightDto, userToBeModified);
        modifiedUser = userDao.updateUser(modifiedUser);
        return entityDtoMapper.toLightDto(modifiedUser);
    }
    @Override
    public void deleteUser(Long userId) {
        userDao.deleteUser(userId);
    }
}
