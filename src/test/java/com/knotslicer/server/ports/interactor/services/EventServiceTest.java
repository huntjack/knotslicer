package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.interactor.EntityCreator;
import com.knotslicer.server.ports.interactor.EntityCreatorImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.DtoCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.DtoCreatorImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollDto;
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
    private ParentService<EventDto> eventService;
    private EntityCreator entityCreator = new EntityCreatorImpl();
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private EntityDtoMapper entityDtoMapper = new EntityDtoMapperImpl(entityCreator, dtoCreator);
    @Mock
    private ChildWithOneRequiredParentDao<Event, User> eventDao;
    @Mock
    private ChildWithOneRequiredParentDao<Poll, Event> pollDao;
    private AutoCloseable closeable;
    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);
        eventService = new EventServiceImpl(entityDtoMapper, eventDao, pollDao);
    }
    @Test
    public void givenCorrectEventId_whenGetWithChildren_thenReturnEventDtoWithPollDtos() {
        Event event = entityCreator.createEvent();
        event.setEventId(1L);
        event.setEventName("Test Event");
        event.setSubject("Test Subject");
        event.setEventDescription("Test Event Description");
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
                .thenReturn(Optional
                        .of(event));
        Long userId = 1L;
        Mockito.when(
                eventDao.getPrimaryParentId(anyLong()))
                .thenReturn(userId);
        EventDto eventDto = eventService.getWithChildren(5L);

        checkEvent(event, eventDto, userId);
        List<PollDto> pollDtos =
                eventDto.getPolls();
        PollDto pollDtoOne = pollDtos.get(0);
        checkPoll(pollOne, pollDtoOne);
        PollDto pollDtoTwo = pollDtos.get(1);
        checkPoll(pollTwo, pollDtoTwo);
    }
    private void checkEvent(Event event, EventDto eventDto, Long userId) {
        assertEquals(event.getEventId(),
                eventDto.getEventId());
        assertEquals(event.getEventName(),
                eventDto.getEventName());
        assertEquals(event.getSubject(),
                eventDto.getSubject());
        assertEquals(event.getEventDescription(),
                eventDto.getEventDescription());
        assertEquals(userId,
                eventDto.getUserId());
    }
    private void checkPoll(Poll poll, PollDto pollDto) {
        assertEquals(poll.getPollId(),
                pollDto.getPollId());
        assertEquals(poll.getStartTimeUtc(),
                pollDto.getStartTimeUtc());
        assertEquals(poll.getEndTimeUtc(),
                pollDto.getEndTimeUtc());
    }
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
