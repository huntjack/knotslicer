package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
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
    private ParentService<MemberDto> memberService;
    private EntityCreator entityCreator = new EntityCreatorImpl();
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private EntityDtoMapper entityDtoMapper = new EntityDtoMapperImpl(entityCreator, dtoCreator);
    @Mock
    private ChildWithTwoParentsDao<Member, User, Project> memberDao;
    @Mock
    private ChildWithOneRequiredParentDao<Schedule, Member> scheduleDao;
    private AutoCloseable closeable;
    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);
        memberService = new MemberServiceImpl(entityDtoMapper, memberDao, scheduleDao);
    }
    @Test
    public void givenCorrectMemberId_whenGetWithChildren_thenReturnMemberDtoWithScheduleDtos() {
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
        Long userId = 10L;
        Mockito.when(
                memberDao.getPrimaryParentId(anyLong()))
                .thenReturn(userId);
        Long projectId = 20L;
        Mockito.when(memberDao.getSecondaryParentId(anyLong()))
                .thenReturn(projectId);
        MemberDto memberDto =
                memberService.getWithChildren(5L);

        checkMemberDto(member,
                memberDto,
                userId,
                projectId);
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
