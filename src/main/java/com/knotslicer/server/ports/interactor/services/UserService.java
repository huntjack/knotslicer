package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.ports.interactor.datatransferobjects.UserDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserLightDto getUser(Long userId);
    UserLightDto updateUser(UserLightDto userDto);
    void deleteUser(Long userId);
}
