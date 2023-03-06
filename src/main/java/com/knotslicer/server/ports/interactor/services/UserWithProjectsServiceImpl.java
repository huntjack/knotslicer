package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Event;
import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Project;
import com.knotslicer.server.domain.User;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ProcessAs(ProcessType.PROJECT)
@ApplicationScoped
public class UserWithProjectsServiceImpl implements UserWithChildrenService {
    private EntityDtoMapper entityDtoMapper;
    private ChildWithOneRequiredParentDao<Project, User> projectDao;
    @Override
    public UserLightDto getUserWithChildren(Long userId) {
        Optional<User> optionalUser =
                projectDao.getPrimaryParentWithChildren(userId);
        User user = unpackOptionalUser(optionalUser);
        UserLightDto userLightDto = entityDtoMapper.toDto(user);
        return entityDtoMapper
                .addProjectDtosToUserLightDto(
                        userLightDto,
                        user);
    }
    private User unpackOptionalUser(Optional<User> optionalUser) {
        return optionalUser.orElseThrow(() -> new EntityNotFoundException("User not found."));
    }
    @Inject
    public UserWithProjectsServiceImpl(EntityDtoMapper entityDtoMapper,
                                       @ProcessAs(ProcessType.PROJECT)
                                       ChildWithOneRequiredParentDao<Project, User> projectDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.projectDao = projectDao;
    }
}
