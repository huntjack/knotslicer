package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Project;
import com.knotslicer.server.domain.User;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.WithChildren;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ProcessAs(ProcessType.USER)
@WithChildren(ProcessType.MEMBER)
@ApplicationScoped
public class UserWithMembersServiceImpl implements UserWithChildrenService {
    private EntityDtoMapper entityDtoMapper;
    private ChildWithTwoParentsDao<Member, User, Project> memberDao;
    @Override
    public UserLightDto getUserWithChildren(Long userId) {
        Optional<User> optionalUser = memberDao.getPrimaryParentWithChildren(userId);
        User user = optionalUser
                .orElseThrow(() -> new EntityNotFoundException());
        UserLightDto userLightDto = entityDtoMapper.toDto(user);
        return entityDtoMapper
                .addMemberDtosToUserLightDto(
                        userLightDto,
                        user);
    }
    @Inject
    public UserWithMembersServiceImpl(EntityDtoMapper entityDtoMapper,
                                      @ProcessAs(ProcessType.MEMBER)
                                      ChildWithTwoParentsDao<Member, User, Project> memberDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.memberDao = memberDao;
    }
    protected UserWithMembersServiceImpl() {}
}
