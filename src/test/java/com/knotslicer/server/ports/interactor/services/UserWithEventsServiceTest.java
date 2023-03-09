package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.entitygateway.EventDao;
import com.knotslicer.server.ports.interactor.EntityCreator;
import com.knotslicer.server.ports.interactor.EntityCreatorImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.DtoCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.DtoCreatorImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
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

public class UserWithEventsServiceTest {
    private UserWithChildrenService userWithEventsService;
    private EntityCreator entityCreator = new EntityCreatorImpl();
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private EntityDtoMapper entityDtoMapper;
    @Mock
    private EventDao eventDao;
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
        userWithEventsService = new UserWithEventsServiceImpl(
                entityDtoMapper,
                eventDao);
    }
    @Test
    public void givenCorrectUserId_whenGetUserWithChildren_thenReturnUserLightDtoWithEventDtos() {
        User user = entityCreator.createUser();
        user.setUserId(1L);
        user.setEmail("example@mail.com");
        user.setUserName("testUser");
        user.setUserDescription("Test User Description");
        user.setTimeZone(
                ZoneId.of("America/Los_Angeles"));
        Event eventOne = entityCreator.createEvent();
        eventOne.setEventId(1L);
        eventOne.setEventName("EventOne Name");
        eventOne.setSubject("EventOne Subject");
        eventOne.setEventDescription("EventOne Description");
        UserImpl userImpl = (UserImpl) user;
        EventImpl eventImplOne = (EventImpl) eventOne;
        userImpl.addEvent(eventImplOne);
        Event eventTwo = entityCreator.createEvent();
        eventTwo.setEventId(2L);
        eventTwo.setEventName("EventTwo Name");
        eventTwo.setSubject("EventTwo Subject");
        eventTwo.setEventDescription("EventTwo Description");
        EventImpl eventImplTwo = (EventImpl) eventTwo;
        userImpl.addEvent(eventImplTwo);

        Mockito.when(eventDao.getPrimaryParentWithChildren(anyLong()))
                .thenReturn(
                        Optional.of(user));
        Long userId = user.getUserId();
        UserLightDto userLightDto =
                userWithEventsService.getUserWithChildren(userId);

        checkUserLightDto(user, userLightDto);
        List<EventDto> eventDtos = userLightDto.getEvents();
        EventDto eventDtoOne = eventDtos.get(0);
        checkEventDto(eventOne, eventDtoOne);
        EventDto eventDtoTwo = eventDtos.get(1);
        checkEventDto(eventTwo, eventDtoTwo);
    }
    private void checkUserLightDto(User user, UserLightDto userLightDto) {
        assertEquals(user.getUserId(), userLightDto.getUserId());
        assertEquals(user.getUserName(), userLightDto.getUserName());
        assertEquals(user.getUserDescription(), userLightDto.getUserDescription());
        assertEquals(user.getTimeZone(), userLightDto.getTimeZone());
    }
    private void checkEventDto(Event event, EventDto eventDto) {
        assertEquals(event.getEventId(),
                eventDto.getEventId());
        assertEquals(event.getEventName(),
                eventDto.getEventName());
        assertEquals(event.getSubject(),
                eventDto.getSubject());
        assertEquals(event.getEventDescription(),
                eventDto.getEventDescription());
    }
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
