package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Project;
import com.knotslicer.server.domain.User;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.entitygateway.MemberDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ProcessAs(ProcessType.MEMBER)
@ApplicationScoped
public class UserWithMembersServiceImpl implements UserWithChildrenService {
    private EntityDtoMapper entityDtoMapper;
    private MemberDao memberDao;
    @Override
    public UserLightDto getUserWithChildren(Long userId) {
        Optional<User> optionalUser = memberDao.getPrimaryParentWithChildren(userId);
        User user = unpackOptionalUser(optionalUser);
        UserLightDto userLightDto = entityDtoMapper.toLightDto(user);
        return entityDtoMapper
                .addMemberDtosToUserLightDto(
                        userLightDto,
                        user);
    }
    private User unpackOptionalUser(Optional<User> optionalUser) {
        return optionalUser.orElseThrow(() -> new EntityNotFoundException("User not found."));
    }
    @Inject
    public UserWithMembersServiceImpl(EntityDtoMapper entityDtoMapper,
                                      MemberDao memberDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.memberDao = memberDao;
    }
    protected UserWithMembersServiceImpl() {}
}
