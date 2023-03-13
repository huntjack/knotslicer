package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Event;
import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Poll;
import com.knotslicer.server.domain.PollAnswer;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.entitygateway.EventDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollAnswerDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import java.util.Optional;

@ProcessAs(ProcessType.POLLANSWER)
@Default
@ApplicationScoped
public class PollAnswerServiceImpl implements Service<PollAnswerDto> {
    private EntityDtoMapper entityDtoMapper;
    private ChildWithTwoParentsDao<PollAnswer,Poll,Member> pollAnswerDao;
    private ChildWithOneRequiredParentDao<Poll, Event> pollDao;
    private EventDao eventDao;
    @Override
    public PollAnswerDto create(PollAnswerDto pollAnswerDto) {
        PollAnswer pollAnswer = entityDtoMapper.toEntity(pollAnswerDto);
        Long pollId = pollAnswerDto.getPollId();
        Long memberId = pollAnswerDto.getMemberId();
        Optional<Event> optionalEvent = pollDao.getPrimaryParent(pollId);
        Event event = optionalEvent
                .orElseThrow(() -> new EntityNotFoundException());
        Long eventId = event.getEventId();
        if(eventDao.eventContainsMember(eventId, memberId)) {
            pollAnswer = pollAnswerDao.create(
                    pollAnswer,
                    pollId,
                    memberId);
            return entityDtoMapper.toDto(
                    pollAnswer,
                    pollId,
                    memberId);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public PollAnswerDto get(Long pollAnswerId) {
        PollAnswer pollAnswer = getPollAnswer(pollAnswerId);
        Long pollId = getPollId(pollAnswerId);
        Long memberId = getMemberId(pollAnswerId);
        return entityDtoMapper
                .toDto(pollAnswer,
                        pollId,
                        memberId);
    }
    private PollAnswer getPollAnswer(Long pollAnswerId) {
        Optional<PollAnswer> optionalPollAnswer =
                pollAnswerDao.get(pollAnswerId);
        return optionalPollAnswer
                .orElseThrow(() -> new EntityNotFoundException());
    }
    private Long getPollId(Long pollAnswerId) {
        Optional<Poll> optionalPoll = pollAnswerDao
                .getPrimaryParent(pollAnswerId);
        Poll poll = optionalPoll
                .orElseThrow(() -> new EntityNotFoundException());
        return poll.getPollId();
    }
    private Long getMemberId(Long pollAnswerId) {
        Optional<Member> optionalMember = pollAnswerDao
                .getSecondaryParent(pollAnswerId);
        Member member = optionalMember
                .orElseThrow(() -> new EntityNotFoundException());
        return member.getMemberId();
    }
    @Override
    public PollAnswerDto update(PollAnswerDto pollAnswerDto) {
        Long pollAnswerId = pollAnswerDto.getPollAnswerId();
        PollAnswer pollAnswerToBeModified = getPollAnswer(pollAnswerId);
        pollAnswerToBeModified = entityDtoMapper
                .toEntity(pollAnswerDto, pollAnswerToBeModified);
        Long pollId = pollAnswerDto.getPollId();
        PollAnswer updatedPollAnswer = pollAnswerDao
                .update(pollAnswerToBeModified, pollId);
        Long memberId = getMemberId(pollAnswerId);
        return entityDtoMapper.toDto(
                updatedPollAnswer,
                pollId,
                memberId);
    }

    @Override
    public void delete(Long pollAnswerId) {
        pollAnswerDao.delete(pollAnswerId);
    }
    @Inject
    public PollAnswerServiceImpl(EntityDtoMapper entityDtoMapper,
                                 @ProcessAs(ProcessType.POLLANSWER)
                                 ChildWithTwoParentsDao<PollAnswer, Poll, Member> pollAnswerDao,
                                 @ProcessAs(ProcessType.POLL)
                                 ChildWithOneRequiredParentDao<Poll, Event> pollDao,
                                 EventDao eventDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.pollAnswerDao = pollAnswerDao;
        this.pollDao = pollDao;
        this.eventDao = eventDao;
    }
    protected PollAnswerServiceImpl() {}
}
