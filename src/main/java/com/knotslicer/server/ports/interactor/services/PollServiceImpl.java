package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Event;
import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Poll;
import com.knotslicer.server.domain.PollAnswer;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ProcessAs(ProcessType.POLL)
@ApplicationScoped
public class PollServiceImpl implements ParentService<PollDto> {
    private EntityDtoMapper entityDtoMapper;
    private ChildWithOneRequiredParentDao<Poll, Event> pollDao;
    private ChildWithTwoParentsDao<PollAnswer, Poll, Member> pollAnswerDao;

    @Override
    public PollDto create(PollDto pollDto) {
        Poll poll = entityDtoMapper.toEntity(pollDto);
        Long eventId = pollDto.getEventId();
        poll = pollDao.create(poll, eventId);
        return entityDtoMapper
                .toDto(poll, eventId);
    }
    @Override
    public PollDto get(Long pollId) {
        Poll poll = getPoll(pollId);
        Long eventId = getEventId(pollId);
        return entityDtoMapper
                .toDto(poll, eventId);
    }
    private Poll getPoll(Long pollId) {
        Optional<Poll> optionalPoll = pollDao.get(pollId);
        return optionalPoll
                .orElseThrow(() -> new EntityNotFoundException());
    }
    private Long getEventId(Long pollId) {
        Optional<Event> optionalEvent = pollDao.getPrimaryParent(pollId);
        Event event = optionalEvent
                .orElseThrow(() -> new EntityNotFoundException());
        return event.getEventId();
    }
    @Override
    public PollDto getWithChildren(Long pollId) {
        Optional<Poll> optionalPoll =
                pollAnswerDao.getPrimaryParentWithChildren(pollId);
        Poll poll = optionalPoll
                .orElseThrow(() -> new EntityNotFoundException());
        Long eventId = getEventId(pollId);
        PollDto pollDto = entityDtoMapper
                .toDto(poll,
                        eventId);
        return entityDtoMapper
                .addPollAnswerDtosToPollDto(pollDto, poll);
    }
    @Override
    public PollDto update(PollDto pollDto) {
        Long pollId = pollDto.getPollId();
        Poll pollLToBeModified = getPoll(pollId);
        pollLToBeModified = entityDtoMapper
                .toEntity(pollDto, pollLToBeModified);
        Long eventId = getEventId(pollId);
        Poll updatedPoll = pollDao
                .update(pollLToBeModified, eventId);
        return entityDtoMapper
                .toDto(updatedPoll, eventId);
    }
    @Override
    public void delete(Long pollId) {
        pollDao.delete(pollId);
    }
    @Inject
    public PollServiceImpl(EntityDtoMapper entityDtoMapper,
                           @ProcessAs(ProcessType.POLL)
                           ChildWithOneRequiredParentDao<Poll, Event> pollDao,
                           @ProcessAs(ProcessType.POLLANSWER)
                               ChildWithTwoParentsDao<PollAnswer, Poll, Member> pollAnswerDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.pollDao = pollDao;
        this.pollAnswerDao = pollAnswerDao;
    }
    protected PollServiceImpl() {}
}
