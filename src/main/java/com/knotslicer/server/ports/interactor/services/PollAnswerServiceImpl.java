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
        PollAnswer pollAnswer =
                unpackOptionalPollAnswer(optionalPollAnswer);
        Long pollId = pollAnswerDao
                .getPrimaryParentId(pollAnswerId);
        Long memberId = pollAnswerDao
                .getSecondaryParentId(pollAnswerId);
        return entityDtoMapper
                .toDto(pollAnswer,
                        pollId,
                        memberId);
    }
    private PollAnswer unpackOptionalPollAnswer(Optional<PollAnswer> optionalPollAnswer) {
        return optionalPollAnswer.orElseThrow(() -> new EntityNotFoundException("Poll answer not found."));
    }

    @Override
    public PollAnswerDto update(PollAnswerDto pollAnswerDto) {
        Long pollAnswerId = pollAnswerDto.getPollAnswerId();
        Optional<PollAnswer> optionalPollAnswer =
                pollAnswerDao.get(pollAnswerId);
        PollAnswer pollAnswerToBeModified =  unpackOptionalPollAnswer(optionalPollAnswer);
        pollAnswerToBeModified = entityDtoMapper
                .toEntity(pollAnswerDto, pollAnswerToBeModified);
        Long pollId = pollAnswerDto.getPollId();
        PollAnswer updatedPollAnswer = pollAnswerDao
                .update(pollAnswerToBeModified, pollId);
        Long memberId = pollAnswerDao
                .getSecondaryParentId(pollAnswerId);
        return entityDtoMapper.toDto(
                updatedPollAnswer,
                pollId,
                memberId);
    }

    @Override
    public void delete(Long pollAnswerId) {
        Long pollId = pollAnswerDao
                .getPrimaryParentId(pollAnswerId);
        pollAnswerDao.delete(
                pollAnswerId,
                pollId);
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
