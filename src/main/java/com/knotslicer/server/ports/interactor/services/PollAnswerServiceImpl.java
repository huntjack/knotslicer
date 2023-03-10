package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Poll;
import com.knotslicer.server.domain.PollAnswer;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollAnswerDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ProcessAs(ProcessType.POLLANSWER)
@ApplicationScoped
public class PollAnswerServiceImpl implements Service<PollAnswerDto> {
    private EntityDtoMapper entityDtoMapper;
    private ChildWithTwoParentsDao<PollAnswer,Poll,Member> pollAnswerDao;
    @Override
    public PollAnswerDto create(PollAnswerDto pollAnswerDto) {
        PollAnswer pollAnswer = entityDtoMapper.toEntity(pollAnswerDto);
        Long pollId = pollAnswerDto.getPollId();
        Long memberId = pollAnswerDto.getMemberId();
        pollAnswer = pollAnswerDao.create(
                pollAnswer,
                pollId,
                memberId);
        return entityDtoMapper.toDto(
                pollAnswer,
                pollId,
                memberId);
    }

    @Override
    public PollAnswerDto get(Long pollAnswerId) {
        Optional<PollAnswer> optionalPollAnswer =
                pollAnswerDao.get(pollAnswerId);
        PollAnswer pollAnswer = optionalPollAnswer
                        .orElseThrow(() -> new EntityNotFoundException());
        Long pollId = getPollId(pollAnswerId);
        Long memberId = getMemberId(pollAnswerId);
        return entityDtoMapper
                .toDto(pollAnswer,
                        pollId,
                        memberId);
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
        Optional<PollAnswer> optionalPollAnswer =
                pollAnswerDao.get(pollAnswerId);
        PollAnswer pollAnswerToBeModified =  optionalPollAnswer
                .orElseThrow(() -> new EntityNotFoundException());
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
                                 ChildWithTwoParentsDao<PollAnswer, Poll, Member> pollAnswerDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.pollAnswerDao = pollAnswerDao;
    }
    protected PollAnswerServiceImpl() {}
}
