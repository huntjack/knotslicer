package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.entitygateway.EventDao;
import com.knotslicer.server.ports.interactor.EntityCreator;
import com.knotslicer.server.ports.interactor.EntityCreatorImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.DtoCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.DtoCreatorImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollAnswerDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapperImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
public class PollAnswerServiceTest {
    private Service<PollAnswerDto> pollAnswerService;
    private EntityCreator entityCreator = new EntityCreatorImpl();
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private EntityDtoMapper entityDtoMapper;
    @Mock
    private ChildWithTwoParentsDao<PollAnswer, Poll, Member> pollAnswerDao;
    @Mock
    private ChildWithTwoParentsDao<Member, User, Project> memberDao;
    @Mock
    private ChildWithOneRequiredParentDao<Poll, Event> pollDao;
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
        pollAnswerService = new PollAnswerServiceImpl(
                entityDtoMapper,
                pollAnswerDao,
                pollDao,
                eventDao);
    }
    @Test
    public void givenCorrectMemberId_whenCreate_thenReturnPollAnswerDto() {
        PollAnswerDto pollAnswerDto = dtoCreator.createPollAnswerDto();
        Event event = entityCreator.createEvent();
        PollAnswer pollAnswer = entityCreator.createPollAnswer();
        initializeDummyData(pollAnswerDto, event, pollAnswer);

        Mockito.when(
                pollDao.getPrimaryParent(anyLong()))
                .thenReturn(
                        Optional.of(event));
        Mockito.when(
                eventDao.eventContainsMember(anyLong(), anyLong()))
                .thenReturn(true);
        Mockito.when(
                pollAnswerDao.create(
                        any(PollAnswer.class),
                        anyLong(),
                        anyLong()))
                .thenReturn(pollAnswer);
        PollAnswerDto pollAnswerDtoResponse =
                pollAnswerService.create(pollAnswerDto);

        assertAll(
                "PollAnswerDtoResponse should have the correct field values.",
                () -> assertEquals(pollAnswerDto.getPollId(),
                        pollAnswerDtoResponse.getPollId()),
                () -> assertEquals(pollAnswerDto.getMemberId(),
                        pollAnswerDtoResponse.getMemberId()),
                () -> assertEquals(pollAnswer.getPollAnswerId(),
                        pollAnswerDtoResponse.getPollAnswerId()),
                () -> assertEquals(pollAnswerDto.isApproved(),
                        pollAnswerDtoResponse.isApproved())
        );
    }
    private void initializeDummyData(PollAnswerDto pollAnswerDto, Event event, PollAnswer pollAnswer) {
        pollAnswerDto.setPollId(1L);
        pollAnswerDto.setMemberId(1L);
        pollAnswerDto.setApproved(true);
        event.setEventId(1L);
        pollAnswer.setApproved(
                pollAnswerDto.isApproved());
        pollAnswer.setPollAnswerId(1L);
    }
    @Test
    public void givenIncorrectMemberId_whenCreate_thenThrowEntityNotFoundException() {
        PollAnswerDto pollAnswerDto = dtoCreator.createPollAnswerDto();
        Event event = entityCreator.createEvent();
        PollAnswer pollAnswer = entityCreator.createPollAnswer();
        initializeDummyData(pollAnswerDto, event, pollAnswer);

        Mockito.when(
                        pollDao.getPrimaryParent(anyLong()))
                .thenReturn(
                        Optional.of(event));
        Mockito.when(
                        eventDao.eventContainsMember(anyLong(), anyLong()))
                .thenReturn(false);
        Mockito.when(
                        pollAnswerDao.create(
                                any(PollAnswer.class),
                                anyLong(),
                                anyLong()))
                .thenReturn(pollAnswer);

        assertThrows(EntityNotFoundException.class,
                () -> pollAnswerService.create(pollAnswerDto),
                "If the member isn't in the event, it should throw an EntityNotFoundException.");
    }
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
