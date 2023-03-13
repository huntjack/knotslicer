package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.ports.entitygateway.UserDao;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import com.knotslicer.server.domain.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class UserServiceImpl implements UserService {
    private EntityDtoMapper entityDtoMapper;
    private UserDao userDao;

    @Override
    public UserLightDto createUser(UserDto userDto) {
        User user = entityDtoMapper.toEntity(userDto);
        user = userDao.create(user);
        return entityDtoMapper.toDto(user);
    }
    @Override
    public UserLightDto getUser(Long userId) {
        Optional<User> optionalUser = userDao.get(userId);
        User user = optionalUser
                .orElseThrow(() -> new EntityNotFoundException());
        return entityDtoMapper.toDto(user);
    }
    @Override
    public UserLightDto updateUser(UserLightDto userLightDto) {
        Long userId = userLightDto.getUserId();
        Optional<User> optionalUser = userDao.get(userId);
        User userToBeModified = optionalUser
                .orElseThrow(() -> new EntityNotFoundException());
        User modifiedUser = entityDtoMapper
                .toEntity(userLightDto, userToBeModified);
        modifiedUser = userDao.update(modifiedUser);
        return entityDtoMapper.toDto(modifiedUser);
    }
    @Override
    public void deleteUser(Long userId) {
        userDao.delete(userId);
    }
    @Inject
    public UserServiceImpl(EntityDtoMapper entityDtoMapper, UserDao userDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.userDao = userDao;
    }
    protected UserServiceImpl() {}
}
