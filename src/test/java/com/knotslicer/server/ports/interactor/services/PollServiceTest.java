package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.interactor.EntityCreator;
import com.knotslicer.server.ports.interactor.EntityCreatorImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.DtoCreator;
import com.knotslicer.server.ports.interactor.datatransferobjects.DtoCreatorImpl;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollAnswerDto;
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

public class PollServiceTest {
    private ParentService<PollDto> pollService;
    private EntityCreator entityCreator = new EntityCreatorImpl();
    private DtoCreator dtoCreator = new DtoCreatorImpl();
    private EntityDtoMapper entityDtoMapper;
    @Mock
    private ChildWithOneRequiredParentDao<Poll, Event> pollDao;
    @Mock
    private ChildWithTwoParentsDao<PollAnswer, Poll, Member> pollAnswerDao;
    @Mock
    private ChildWithTwoParentsDao<Member, User, Project> memberDao;
    private AutoCloseable closeable;
    @BeforeEach
    public void init() {
        closeable = MockitoAnnotations.openMocks(this);
        entityDtoMapper = new EntityDtoMapperImpl(
                entityCreator,
                dtoCreator,
                memberDao,
                pollAnswerDao);
        pollService = new PollServiceImpl(
                entityDtoMapper,
                pollDao,
                pollAnswerDao);
    }

    @Test
    public void givenCorrectPollId_whenGetWithChildren_thenReturnPollDtoWithPollAnswerDtos(){
        Event event = entityCreator.createEvent();
        event.setEventId(1L);
        Poll poll = entityCreator.createPoll();
        poll.setPollId(1L);
        poll.setStartTimeUtc(
                LocalDateTime.of(2022, Month.DECEMBER, 27, 16, 0));
        poll.setEndTimeUtc(
                LocalDateTime.of(2022, Month.DECEMBER, 27, 21, 0));
        Member memberOne = entityCreator.createMember();
        memberOne.setMemberId(1L);
        PollAnswer pollAnswerOne = entityCreator.createPollAnswer();
        pollAnswerOne.setPollAnswerId(1L);
        pollAnswerOne.setApproved(true);
        PollImpl pollImpl = (PollImpl) poll;
        PollAnswerImpl pollAnswerOneImpl = (PollAnswerImpl) pollAnswerOne;
        pollImpl.addPollAnswer(pollAnswerOneImpl);
        MemberImpl memberImplOne = (MemberImpl) entityCreator.createMember();
        memberImplOne.addPollAnswer(pollAnswerOneImpl);
        Member memberTwo = entityCreator.createMember();
        memberTwo.setMemberId(2L);
        PollAnswer pollAnswerTwo = entityCreator.createPollAnswer();
        pollAnswerTwo.setPollAnswerId(2L);
        pollAnswerTwo.setApproved(false);
        PollAnswerImpl pollAnswerTwoImpl = (PollAnswerImpl) pollAnswerTwo;
        pollImpl.addPollAnswer(pollAnswerTwoImpl);
        MemberImpl memberImplTwo = (MemberImpl) entityCreator.createMember();
        memberImplTwo.addPollAnswer(pollAnswerTwoImpl);

        Mockito.when(
                pollAnswerDao.getPrimaryParentWithChildren(anyLong()))
                .thenReturn(Optional
                        .of(poll));
        Mockito.when(
                pollDao.getPrimaryParent(anyLong()))
                .thenReturn(
                        Optional.of(event));
        Mockito.when(pollAnswerDao
                .getSecondaryParent(
                        pollAnswerOne.getPollAnswerId()))
                .thenReturn(
                        Optional.of(memberOne));
        Mockito.when(pollAnswerDao
                        .getSecondaryParent(
                                pollAnswerTwo.getPollAnswerId()))
                .thenReturn(
                        Optional.of(memberTwo));
        PollDto pollDto = pollService.getWithChildren(5L);

        checkPollDto(
                poll,
                pollDto,
                event.getEventId());
        List<PollAnswerDto> pollAnswerDtos =
                pollDto.getPollAnswers();
        PollAnswerDto pollAnswerDtoOne = pollAnswerDtos.get(0);
        Long memberOneId = memberOne.getMemberId();
        checkPollAnswerDto(
                pollAnswerOne,
                pollAnswerDtoOne,
                memberOneId);
        PollAnswerDto pollAnswerDtoTwo = pollAnswerDtos.get(1);
        Long memberTwoId = memberTwo.getMemberId();
        checkPollAnswerDto(
                pollAnswerTwo,
                pollAnswerDtoTwo,
                memberTwoId);
    }
    private void checkPollDto(Poll poll, PollDto pollDto, Long eventId) {
        assertEquals(poll.getPollId(),
                pollDto.getPollId());
        assertEquals(poll.getStartTimeUtc(),
                pollDto.getStartTimeUtc());
        assertEquals(poll.getEndTimeUtc(),
                pollDto.getEndTimeUtc());
        assertEquals(eventId,
                pollDto.getEventId());
    }
    private void checkPollAnswerDto(PollAnswer pollAnswer, PollAnswerDto pollAnswerDto, Long memberId) {
        assertEquals(pollAnswer.getPollAnswerId(),
                pollAnswerDto.getPollAnswerId());
        assertEquals(memberId,
                pollAnswerDto.getMemberId());
        assertEquals(pollAnswer.isApproved(),
                pollAnswerDto.isApproved());
    }
    @AfterEach
    public void shutdown() throws Exception {
        closeable.close();
    }
}
