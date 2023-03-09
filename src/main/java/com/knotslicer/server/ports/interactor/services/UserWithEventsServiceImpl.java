package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Event;
import com.knotslicer.server.domain.User;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.entitygateway.EventDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ProcessAs(ProcessType.EVENT)
@ApplicationScoped
public class UserWithEventsServiceImpl implements UserWithChildrenService {
    private EntityDtoMapper entityDtoMapper;
    private EventDao eventDao;
    @Override
    public UserLightDto getUserWithChildren(Long userId) {
        Optional<User> optionalUser =
                eventDao.getPrimaryParentWithChildren(userId);
        User user = optionalUser
                .orElseThrow(() -> new EntityNotFoundException());
        UserLightDto userLightDto = entityDtoMapper.toDto(user);
        return entityDtoMapper
                .addEventDtosToUserLightDto(
                        userLightDto,
                        user);
    }
    @Inject
    public UserWithEventsServiceImpl(EntityDtoMapper entityDtoMapper,
                                     EventDao eventDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.eventDao = eventDao;
    }
}
