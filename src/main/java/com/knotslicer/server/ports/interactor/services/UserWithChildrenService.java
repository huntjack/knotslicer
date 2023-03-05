package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;

public interface UserWithChildrenService {
    UserLightDto getUserWithChildren(Long userId);
}
