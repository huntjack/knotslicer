package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;

public interface UserWithChildrenService {
    UserLightDto getWithProjects(Long userId);
    UserLightDto getWithMembers(Long userId);
    UserLightDto getWithEvents(Long userId);
}
