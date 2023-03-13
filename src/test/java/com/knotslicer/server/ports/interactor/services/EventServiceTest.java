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

public class EventServiceTest {
    private EventService eventService;
    private EntityCreator entityCreator = new EntityCreatorImpl();
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private EntityDtoMapper entityDtoMapper;
    @Mock
    private EventDao eventDao;
    @Mock
    private ChildWithOneRequiredParentDao<Poll, Event> pollDao;
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
        eventService = new EventServiceImpl(
                entityDtoMapper,
                eventDao,
                pollDao);
    }
    @Test
    public void givenCorrectEventId_whenGetWithChildren_thenReturnEventDtoWithPollDtos() {
        User user = entityCreator.createUser();
        user.setUserId(1L);
        Event event = entityCreator.createEvent();
        initializeEventFields(event);
        Poll pollOne = entityCreator.createPoll();
        pollOne.setPollId(1L);
        pollOne.setStartTimeUtc(
                LocalDateTime.of(2022, Month.DECEMBER, 27, 16, 0));
        pollOne.setEndTimeUtc(
                LocalDateTime.of(2022, Month.DECEMBER, 27, 21, 0));
        EventImpl eventImpl = (EventImpl) event;
        PollImpl pollOneImpl = (PollImpl) pollOne;
        eventImpl.addPoll(pollOneImpl);
        Poll pollTwo = entityCreator.createPoll();
        pollTwo.setPollId(2L);
        pollTwo.setStartTimeUtc(
                LocalDateTime.of(2022, Month.DECEMBER, 27, 20, 0));
        pollTwo.setEndTimeUtc(
                LocalDateTime.of(2022, Month.DECEMBER, 28, 1, 0));
        PollImpl pollTwoImpl = (PollImpl) pollTwo;
        eventImpl.addPoll(pollTwoImpl);

        Mockito.when(
                pollDao.getPrimaryParentWithChildren(anyLong()))
                .thenReturn(
                        Optional.of(event));
        Mockito.when(
                eventDao.getPrimaryParent(anyLong()))
                .thenReturn(
                        Optional.of(user));
        EventDto eventDto = eventService.getWithChildren(1L);

        checkEventDto(
                event,
                eventDto,
                user.getUserId());
        List<PollDto> pollDtos =
                eventDto.getPolls();
        PollDto pollDtoOne = pollDtos.get(0);
        checkPollDto(pollOne, pollDtoOne);
        PollDto pollDtoTwo = pollDtos.get(1);
        checkPollDto(pollTwo, pollDtoTwo);
    }
    private void initializeEventFields(Event event) {
        event.setEventId(1L);
        event.setEventName("Test Event");
        event.setSubject("Test Subject");
        event.setEventDescription("Test Event Description");
    }
    private void checkEventDto(Event event, EventDto eventDto, Long userId) {
        assertAll(
                "EventDto should have the correct field values.",
                () -> assertEquals(event.getEventId(),
                        eventDto.getEventId()),
                () -> assertEquals(event.getEventName(),
                        eventDto.getEventName()),
                () -> assertEquals(event.getSubject(),
                        eventDto.getSubject()),
                () -> assertEquals(event.getEventDescription(),
                        eventDto.getEventDescription()),
                () -> assertEquals(userId,
                        eventDto.getUserId())
        );
    }
    private void checkPollDto(Poll poll, PollDto pollDto) {
        assertAll(
                "PollDto should have the correct field values.",
                () -> assertEquals(poll.getPollId(),
                        pollDto.getPollId()),
                () -> assertEquals(poll.getStartTimeUtc(),
                        pollDto.getStartTimeUtc()),
                () -> assertEquals(poll.getEndTimeUtc(),
                        pollDto.getEndTimeUtc())
        );
    }
    @Test
    public void givenCorrectEventId_whenGetWithMembers_thenReturnEventDtoWithMemberDtos() {
        User userOne = entityCreator.createUser();
        userOne.setUserId(1L);
        Project project = entityCreator.createProject();
        project.setProjectId(1L);
        Event event = entityCreator.createEvent();
        initializeEventFields(event);
        Member memberOne = entityCreator.createMember();
        memberOne.setMemberId(1L);
        memberOne.setName("memberOne Name");
        memberOne.setRole("memberOne Role");
        memberOne.setRoleDescription("memberOne Role Description");
        EventImpl eventImpl = (EventImpl) event;
        MemberImpl memberOneImpl = (MemberImpl) memberOne;
        memberOneImpl.addEvent(eventImpl);
        User userTwo = entityCreator.createUser();
        userTwo.setUserId(2L);
        Member memberTwo = entityCreator.createMember();
        memberTwo.setMemberId(2L);
        memberTwo.setName("memberTwo Name");
        memberTwo.setRole("memberTwo Role");
        memberTwo.setRoleDescription("memberTwo Role Description");
        MemberImpl memberTwoImpl = (MemberImpl) memberTwo;
        memberTwoImpl.addEvent(eventImpl);

        Mockito.when(
                eventDao.getPrimaryParent(anyLong()))
                .thenReturn(
                        Optional.of(userOne));
        Mockito.when(
                eventDao.getEventWithMembers(anyLong()))
                .thenReturn(
                        Optional.of(event));
        Long memberOneId = memberOne.getMemberId();
        Mockito.when(
                memberDao.getPrimaryParent(memberOneId))
                .thenReturn(
                        Optional.of(userOne));
        Long memberTwoId = memberTwo.getMemberId();
        Mockito.when(
                memberDao.getPrimaryParent(
                        memberTwoId))
                .thenReturn(
                        Optional.of(userTwo));
        Mockito.when(
                memberDao.getSecondaryParent(anyLong()))
                .thenReturn(Optional.of(project));
        EventDto eventDto = eventService.getWithMembers(
                event.getEventId());

        checkEventDto(
                event,
                eventDto,
                userOne.getUserId());
        List<MemberDto> memberDtos = eventDto.getMembers();
        Long userOneId = userOne.getUserId();
        Long projectId = project.getProjectId();
        Long userTwoId = userTwo.getUserId();
        assertEquals(memberDtos.size(), 2);
        for(MemberDto memberDto: memberDtos) {
            Long memberDtoId = memberDto.getMemberId();
            if(memberDtoId.equals(memberOneId)) {
                checkMemberDto(memberOne, memberDto, userOneId, projectId);
            } else  {
                checkMemberDto(memberTwo, memberDto, userTwoId, projectId);
            }
        }
    }
    private void checkMemberDto(Member member, MemberDto memberDto, Long userId, Long projectId) {
        assertAll(
                "MemberDto should have the correct field values.",
                () -> assertEquals(member.getMemberId(),
                        memberDto.getMemberId()),
                () -> assertEquals(userId,
                        memberDto.getUserId()),
                () -> assertEquals(projectId,
                        memberDto.getProjectId()),
                () -> assertEquals(member.getName(),
                        memberDto.getName()),
                () -> assertEquals(member.getRole(),
                        memberDto.getRole()),
                () -> assertEquals(member.getRoleDescription(),
                        memberDto.getRoleDescription())
        );
    }
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
