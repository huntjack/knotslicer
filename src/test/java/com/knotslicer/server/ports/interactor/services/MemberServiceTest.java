package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.entitygateway.MemberDao;
import com.knotslicer.server.ports.interactor.EntityCreator;
import com.knotslicer.server.ports.interactor.EntityCreatorImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.DtoCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.DtoCreatorImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.ScheduleDto;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapperImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

public class MemberServiceTest {
    private MemberService memberService;
    private EntityCreator entityCreator = new EntityCreatorImpl();
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private EntityDtoMapper entityDtoMapper;
    @Mock
    private MemberDao memberDao;
    @Mock
    private ChildWithOneRequiredParentDao<Schedule, Member> scheduleDao;
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
        memberService = new MemberServiceImpl(
                entityDtoMapper,
                memberDao,
                scheduleDao);
    }
    @Test
    public void givenCorrectMemberId_whenGetWithChildren_thenReturnMemberDtoWithScheduleDtos() {
        User user = entityCreator.createUser();
        user.setUserId(10L);
        Project project = entityCreator.createProject();
        project.setProjectId(20L);
        Member member = entityCreator.createMember();
        member.setMemberId(1L);
        member.setName("member1");
        member.setRole("member1 role");
        member.setRoleDescription("member1 role description");
        Schedule scheduleOne = entityCreator.createSchedule();
        scheduleOne.setScheduleId(1L);
        scheduleOne.setStartTimeUtc(
                LocalDateTime.of(2022, Month.DECEMBER, 27, 16, 0));
        scheduleOne.setEndTimeUtc(
                LocalDateTime.of(2022, Month.DECEMBER, 27, 21, 0));
        MemberImpl memberImpl = (MemberImpl) member;
        ScheduleImpl scheduleImpl1 = (ScheduleImpl) scheduleOne;
        memberImpl.addSchedule(scheduleImpl1);
        Schedule scheduleTwo = entityCreator.createSchedule();
        scheduleTwo.setScheduleId(2L);
        scheduleTwo.setStartTimeUtc(
                LocalDateTime.of(2022, Month.DECEMBER, 27, 20, 0));
        scheduleTwo.setEndTimeUtc(
                LocalDateTime.of(2022, Month.DECEMBER, 28, 1, 0));
        ScheduleImpl scheduleImpl2 = (ScheduleImpl) scheduleTwo;
        memberImpl.addSchedule(scheduleImpl2);

        Mockito.when(
                scheduleDao.getPrimaryParentWithChildren(anyLong()))
                .thenReturn(Optional
                        .of(member));
        Mockito.when(
                memberDao.getPrimaryParent(anyLong()))
                .thenReturn(user);
        Mockito.when(memberDao.getSecondaryParent(anyLong()))
                .thenReturn(project);
        MemberDto memberDto =
                memberService.getWithChildren(5L);

        checkMemberDto(member,
                memberDto,
                user.getUserId(),
                project.getProjectId());
        List<ScheduleDto> scheduleDtos =
                memberDto.getSchedules();
        ScheduleDto scheduleDtoOne = scheduleDtos.get(0);
        checkScheduleDto(scheduleOne, scheduleDtoOne);
        ScheduleDto scheduleDtoTwo = scheduleDtos.get(1);
        checkScheduleDto(scheduleTwo, scheduleDtoTwo);
    }
    private void checkMemberDto(Member member, MemberDto memberDto, Long userId, Long projectId) {
        assertEquals(member.getMemberId(),
                memberDto.getMemberId());
        assertEquals(member.getName(),
                memberDto.getName());
        assertEquals(member.getRole(),
                memberDto.getRole());
        assertEquals(member.getRoleDescription(),
                memberDto.getRoleDescription());
        assertEquals(userId,
                memberDto.getUserId());
        assertEquals(projectId,
                memberDto.getProjectId());
    }
    private void checkScheduleDto(Schedule schedule, ScheduleDto scheduleDto) {
        assertEquals(schedule.getScheduleId(),
                scheduleDto.getScheduleId());
        assertEquals(schedule.getStartTimeUtc(),
                scheduleDto.getStartTimeUtc());
        assertEquals(schedule.getEndTimeUtc(),
                scheduleDto.getEndTimeUtc());
    }
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
