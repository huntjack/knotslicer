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

@ApplicationScoped
public class UserWithChildrenServiceImpl implements UserWithChildrenService {
    private EntityDtoMapper entityDtoMapper;
    private ChildWithOneRequiredParentDao<Project, User> projectDao;
    private ChildWithTwoParentsDao<Member,User,Project> memberDao;
    private ChildWithOneRequiredParentDao<Event, User> eventDao;
    @Override
    public UserLightDto getWithProjects(Long userId) {
        Optional<User> optionalUser =
                projectDao.getPrimaryParentWithChildren(userId);
        User user = unpackOptionalUser(optionalUser);
        UserLightDto userLightDto =
                entityDtoMapper.toLightDto(user);
        return entityDtoMapper
                .addProjectDtosToUserLightDto(
                        userLightDto,
                        user);
    }
    private User unpackOptionalUser(Optional<User> optionalUser) {
        return optionalUser.orElseThrow(() -> new EntityNotFoundException("User not found."));
    }

    @Override
    public UserLightDto getWithMembers(Long userId) {
        Optional<User> optionalUser = memberDao.getPrimaryParentWithChildren(userId);
        User user = unpackOptionalUser(optionalUser);
        UserLightDto userLightDto = entityDtoMapper.toLightDto(user);

        return null;
    }

    @Override
    public UserLightDto getWithEvents(Long userId) {
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
    @Inject
    public UserWithChildrenServiceImpl(EntityDtoMapper entityDtoMapper,
                                       @ProcessAs(ProcessType.PROJECT)
                                       ChildWithOneRequiredParentDao<Project, User> projectDao,
                                       @ProcessAs(ProcessType.MEMBER)
                                           ChildWithTwoParentsDao<Member,User,Project> memberDao,
                                       @ProcessAs(ProcessType.EVENT)
                                           ChildWithOneRequiredParentDao<Event, User> eventDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.projectDao = projectDao;
        this.memberDao = memberDao;
        this.eventDao = eventDao;
    }
}
