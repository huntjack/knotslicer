package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.interactor.EntityCreator;
import com.knotslicer.server.ports.interactor.EntityCreatorImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.DtoCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.DtoCreatorImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.UserLightDto;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapperImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;

public class UserWithMembersServiceTest {
    private UserWithChildrenService userWithMembersService;
    private EntityCreator entityCreator = new EntityCreatorImpl();
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private EntityDtoMapper entityDtoMapper;
    @Mock
    private ChildWithTwoParentsDao<Member, User, Project> memberDao;
    @Mock
    private ChildWithTwoParentsDao<PollAnswer, Poll, Member> pollAnswerDao;
    private AutoCloseable closeable;

    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);
        entityDtoMapper = new EntityDtoMapperImpl(
                entityCreator,
                dtoCreator,
                memberDao,
                pollAnswerDao);
        userWithMembersService = new UserWithMembersServiceImpl(
                entityDtoMapper,
                memberDao);
    }
    @Test
    public void givenCorrectUserId_whenGetUserWithChildren_thenReturnUserLightDtoWithMemberDtos() {
        User user = entityCreator.createUser();
        user.setUserId(1L);
        user.setEmail("example@mail.com");
        user.setUserName("testUser");
        user.setUserDescription("Test User Description");
        user.setTimeZone(
                ZoneId.of("America/Los_Angeles"));
        Project projectOne = entityCreator.createProject();
        projectOne.setProjectId(1L);
        Member memberOne = entityCreator.createMember();
        memberOne.setMemberId(1L);
        memberOne.setName("MemberOne Name");
        memberOne.setRole("MemberOne Role");
        memberOne.setRoleDescription("MemberOne Role Description");
        UserImpl userImpl = (UserImpl) user;
        MemberImpl memberOneImpl = (MemberImpl) memberOne;
        userImpl.addMember(memberOneImpl);
        Project projectTwo = entityCreator.createProject();
        projectTwo.setProjectId(2L);
        Member memberTwo = entityCreator.createMember();
        memberTwo.setMemberId(2L);
        memberTwo.setName("MemberTwo Name");
        memberTwo.setRole("MemberTwo Role");
        memberTwo.setRoleDescription("MemberTwo Role Description");
        MemberImpl memberTwoImpl = (MemberImpl) memberTwo;
        userImpl.addMember(memberTwoImpl);

        Mockito.when(
                memberDao.getPrimaryParentWithChildren(anyLong()))
                .thenReturn(Optional.of(user));
        Mockito.when(
                memberDao.getSecondaryParent(
                        memberOne.getMemberId()))
                .thenReturn(
                        Optional.of(projectOne));
        Mockito.when(
                memberDao.getSecondaryParent(
                        memberTwo.getMemberId()))
                .thenReturn(
                        Optional.of(projectTwo));
        Long userId = user.getUserId();
        UserLightDto userLightDto = userWithMembersService.getUserWithChildren(userId);

        checkUserLightDto(user, userLightDto);
        List<MemberDto> memberDtos = userLightDto.getMembers();
        MemberDto memberDtoOne = memberDtos.get(0);
        Long projectOneId = projectOne.getProjectId();
        checkMemberDto(
                memberOne,
                memberDtoOne,
                projectOneId);
        MemberDto memberDtoTwo = memberDtos.get(1);
        Long projectTwoId = projectTwo.getProjectId();
        checkMemberDto(
                memberTwo,
                memberDtoTwo,
                projectTwoId);
    }
    private void checkUserLightDto(User user, UserLightDto userLightDto) {
        assertEquals(user.getUserId(), userLightDto.getUserId());
        assertEquals(user.getUserName(), userLightDto.getUserName());
        assertEquals(user.getUserDescription(), userLightDto.getUserDescription());
        assertEquals(user.getTimeZone(), userLightDto.getTimeZone());
    }
    private void checkMemberDto(Member member, MemberDto memberDto, Long projectId) {
        assertEquals(member.getMemberId(), memberDto.getMemberId());
        assertEquals(projectId, memberDto.getProjectId());
        assertEquals(member.getName(), memberDto.getName());
        assertEquals(member.getRole(), memberDto.getRole());
        assertEquals(member.getRoleDescription(), memberDto.getRoleDescription());
    }
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
