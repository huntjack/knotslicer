package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.MemberImpl;
import com.knotslicer.server.domain.Schedule;
import com.knotslicer.server.domain.ScheduleImpl;
import com.knotslicer.server.ports.entitygateway.MemberDao;
import com.knotslicer.server.ports.entitygateway.ScheduleDao;
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
    private MemberDao memberDao;
    @Mock
    private ScheduleDao scheduleDao;
    private AutoCloseable closeable;
    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);
        memberService = new MemberServiceImpl(entityDtoMapper, memberDao, scheduleDao);
    }
    @Test
    public void givenCorrectMemberId_whenGetWithChildren_thenReturnMemberDtoWithScheduleDtos() {
        Member member = entityCreator.createMember();
        member.setName("member1");
        member.setRole("member1 role");
        member.setRoleDescription("member1 role description");
        Schedule schedule1 = entityCreator.createSchedule();
        schedule1.setStartTimeUtc(LocalDateTime.of(2022, Month.DECEMBER, 27, 16, 0));
        schedule1.setEndTimeUtc(LocalDateTime.of(2022, Month.DECEMBER, 27, 21, 0));
        MemberImpl memberImpl = (MemberImpl) member;
        ScheduleImpl scheduleImpl1 = (ScheduleImpl) schedule1;
        memberImpl.addSchedule(scheduleImpl1);
        Schedule schedule2 = entityCreator.createSchedule();
        schedule2.setStartTimeUtc(LocalDateTime.of(2022, Month.DECEMBER, 27, 20, 0));
        schedule2.setEndTimeUtc(LocalDateTime.of(2022, Month.DECEMBER, 28, 1, 0));
        ScheduleImpl scheduleImpl2 = (ScheduleImpl) schedule2;
        memberImpl.addSchedule(scheduleImpl2);

        Mockito.when(
                scheduleDao.getPrimaryParentWithChildren(anyLong()))
                .thenReturn(Optional
                        .ofNullable(member));
        Long userId = 10L;
        Mockito.when(
                memberDao.getPrimaryParentId(anyLong()))
                .thenReturn(userId);
        Long projectId = 20L;
        Mockito.when(memberDao.getSecondaryParentId(anyLong()))
                .thenReturn(projectId);

        MemberDto memberDto =
                memberService.getWithChildren(5L);
        checkMember(memberDto,
                member,
                userId,
                projectId);

        List<ScheduleDto> scheduleDtos =
                memberDto.getSchedules();
        ScheduleDto scheduleDto1 = scheduleDtos.get(0);
        checkSchedule(scheduleDto1, schedule1);

        ScheduleDto scheduleDto2 = scheduleDtos.get(1);
        checkSchedule(scheduleDto2, schedule2);
    }
    private void checkMember(MemberDto memberDto, Member member, Long userId, Long projectId) {
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
    private void checkSchedule(ScheduleDto scheduleDto, Schedule schedule) {
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
