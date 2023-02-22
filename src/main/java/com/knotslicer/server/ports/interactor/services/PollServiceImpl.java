package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Poll;
import com.knotslicer.server.ports.entitygateway.PollDao;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@PollService
@ApplicationScoped
public class PollServiceImpl implements ParentService<PollDto> {
    private EntityDtoMapper entityDtoMapper;
    private PollDao pollDao;

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
        Optional<Poll> optionalPoll = pollDao.get(pollId);
        Poll poll = unpackOptionalPoll(optionalPoll);
        Long eventId = pollDao.getPrimaryParentId(pollId);
        return entityDtoMapper
                .toDto(poll, eventId);
    }
    private Poll unpackOptionalPoll(Optional<Poll> optionalPoll) {
        return optionalPoll.orElseThrow(() -> new EntityNotFoundException("Poll not found."));
    }
    @Override
    public PollDto getWithChildren(Long pollId) {
        return null;
    }
    @Override
    public PollDto update(PollDto pollDto) {
        return null;
    }
    @Override
    public void delete(Long pollId) {
        Long eventId = pollDao.getPrimaryParentId(pollId);
        pollDao.delete(pollId, eventId);
    }
    @Inject
    public PollServiceImpl(EntityDtoMapper entityDtoMapper, PollDao pollDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.pollDao = pollDao;
    }
    protected PollServiceImpl() {}
}
