package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Event;
import com.knotslicer.server.domain.User;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
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
    private ChildWithOneRequiredParentDao<Event, User> eventDao;
    @Override
    public UserLightDto getUserWithChildren(Long userId) {
        Optional<User> optionalUser =
                eventDao.getPrimaryParentWithChildren(userId);
        User user = unpackOptionalUser(optionalUser);
        UserLightDto userLightDto =
                entityDtoMapper.toLightDto(user);
        return entityDtoMapper
                .addEventDtosToUserLightDto(
                        userLightDto,
                        user);
    }
    private User unpackOptionalUser(Optional<User> optionalUser) {
        return optionalUser.orElseThrow(() -> new EntityNotFoundException("User not found."));
    }
    @Inject
    public UserWithEventsServiceImpl(EntityDtoMapper entityDtoMapper,
                                     @ProcessAs(ProcessType.EVENT)
                                     ChildWithOneRequiredParentDao<Event, User> eventDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.eventDao = eventDao;
    }
}
