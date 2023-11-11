package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.entitygateway.EventDao;
import com.knotslicer.server.ports.interactor.EntityCreator;
import com.knotslicer.server.ports.interactor.EntityCreatorImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.*;
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
    private ChildWithTwoParentsDao<Member, User, Project> memberDao;
    @Mock
    private ChildWithOneRequiredParentDao<Schedule, Member> scheduleDao;
    @Mock
    private ChildWithTwoParentsDao<PollAnswer, Poll, Member> pollAnswerDao;
    @Mock
    private EventDao eventDao;
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
                scheduleDao,
                eventDao);
    }
    @Test
    public void givenCorrectMemberId_whenGetWithChildren_thenReturnMemberDtoWithScheduleDtos() {
        User user = entityCreator.createUser();
        user.setUserId(10L);
        Project project = entityCreator.createProject();
        project.setProjectId(20L);
        Member member = entityCreator.createMember();
        initializeMember(member);
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
                .thenReturn(
                        Optional.of(user));
        Mockito.when(memberDao.getSecondaryParent(anyLong()))
                .thenReturn(
                        Optional.of(project));
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
    private void initializeMember(Member member) {
        member.setMemberId(1L);
        member.setName("member1");
        member.setRole("member1 role");
        member.setRoleDescription("member1 role description");
    }
    private void checkMemberDto(Member member, MemberDto memberDto, Long userId, Long projectId) {
        assertAll(
                "MemberDto should have the correct field values.",
                () -> assertEquals(member.getMemberId(),
                        memberDto.getMemberId()),
                () -> assertEquals(member.getName(),
                        memberDto.getName()),
                () -> assertEquals(member.getRole(),
                        memberDto.getRole()),
                () -> assertEquals(member.getRoleDescription(),
                        memberDto.getRoleDescription()),
                () ->assertEquals(userId,
                        memberDto.getUserId()),
                () -> assertEquals(projectId,
                        memberDto.getProjectId())
        );
    }
    private void checkScheduleDto(Schedule schedule, ScheduleDto scheduleDto) {
        assertAll(
                "ScheduleDto should have the correct field values.",
                () -> assertEquals(schedule.getScheduleId(),
                        scheduleDto.getScheduleId()),
                () -> assertEquals(schedule.getStartTimeUtc(),
                        scheduleDto.getStartTimeUtc()),
                () -> assertEquals(schedule.getEndTimeUtc(),
                        scheduleDto.getEndTimeUtc())
        );
    }
    @Test
    public void givenCorrectMemberId_whenGetWithEvents_thenReturnMemberDtoWithEventDtos() {
        User user = entityCreator.createUser();
        user.setUserId(1L);
        Project project = entityCreator.createProject();
        project.setProjectId(1L);
        Member member = entityCreator.createMember();
        initializeMember(member);
        Event eventOne = entityCreator.createEvent();
        eventOne.setEventId(1L);
        eventOne.setEventName("eventOne Name");
        eventOne.setSubject("eventOne Subject");
        eventOne.setEventDescription("eventOne Description");
        MemberImpl memberImpl = (MemberImpl) member;
        EventImpl eventImplOne = (EventImpl) eventOne;
        memberImpl.addEvent(eventImplOne);
        Event eventTwo = entityCreator.createEvent();
        eventTwo.setEventId(2L);
        eventTwo.setEventName("eventTwo Name");
        eventTwo.setSubject("eventTwo Subject");
        eventTwo.setEventDescription("eventTwo Description");
        EventImpl eventImplTwo = (EventImpl) eventTwo;
        memberImpl.addEvent(eventImplTwo);

        Mockito.when(
                eventDao.getMemberWithEvents(anyLong()))
                .thenReturn(
                        Optional.of(member));
        Mockito.when(
                memberDao.getPrimaryParent(anyLong()))
                .thenReturn(
                        Optional.of(user));
        Mockito.when(
                memberDao.getSecondaryParent(anyLong()))
                .thenReturn(
                        Optional.of(project));
        MemberDto memberDto = memberService.getWithEvents(
                member.getMemberId());

        checkMemberDto(member,
                memberDto,
                user.getUserId(),
                project.getProjectId());
        List<EventDto> eventDtos = memberDto.getEvents();
        assertEquals(eventDtos.size(), 2);
        Long eventOneId = eventOne.getEventId();
        for(EventDto eventDto: eventDtos) {
            Long eventDtoId = eventDto.getEventId();
            if(eventDtoId.equals(eventOneId)) {
                checkEvent(eventOne, eventDto);
            } else {
                checkEvent(eventTwo, eventDto);
            }
        }
    }
    private void checkEvent(Event event, EventDto eventDto) {
        assertAll(
                "Event should have the correct field values.",
                () -> assertEquals(event.getEventId(),
                        eventDto.getEventId()),
                () -> assertEquals(event.getEventName(),
                        eventDto.getEventName()),
                () -> assertEquals(event.getSubject(),
                        eventDto.getSubject()),
                () -> assertEquals(event.getEventDescription(),
                        eventDto.getEventDescription())
        );
    }
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
