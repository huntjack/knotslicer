package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.ports.interactor.datatransferobjects.UserDto;

public interface UserService {
    public UserDto createUser(UserDto userDto);
    public UserDto getUser(Long userId);
    public void deleteUser(Long userId);
}
