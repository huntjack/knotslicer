package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Event;
import com.knotslicer.server.domain.Poll;
import com.knotslicer.server.domain.User;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.entitygateway.EventDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventMemberDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class EventServiceImpl implements EventService {
    private EntityDtoMapper entityDtoMapper;
    private EventDao eventDao;
    private ChildWithOneRequiredParentDao<Poll, Event> pollDao;

    @Override
    public EventDto create(EventDto eventDto) {
        Event event = entityDtoMapper.toEntity(eventDto);
        Long userId = eventDto.getUserId();
        event = eventDao.create(event, userId);
        return entityDtoMapper
                .toDto(event, userId);
    }
    @Override
    public EventDto get(Long eventId) {
        Optional<Event> optionalEvent = eventDao.get(eventId);
        Event event = optionalEvent
                .orElseThrow(() -> new EntityNotFoundException());
        Optional<User> optionalUser = eventDao.getPrimaryParent(eventId);
        User user = optionalUser
                .orElseThrow(() -> new EntityNotFoundException());
        Long userId = user.getUserId();
        return entityDtoMapper
                .toDto(event, userId);
    }
    @Override
    public EventDto getWithChildren(Long eventId) {
        Optional<Event> optionalEvent =
                pollDao.getPrimaryParentWithChildren(eventId);
        Event event = optionalEvent
                .orElseThrow(() -> new EntityNotFoundException());
        Optional<User> optionalUser = eventDao
                .getPrimaryParent(eventId);
        User user = optionalUser
                .orElseThrow(() -> new EntityNotFoundException());
        Long userId = user.getUserId();
        EventDto eventDto = entityDtoMapper
                .toDto(event, userId);
        return entityDtoMapper
                .addPollDtosToEventDto(eventDto, event);
    }
    @Override
    public EventDto getWithMembers(Long eventId) {
        return null;
    }

    @Override
    public EventDto update(EventDto eventDto) {
        Long eventId = eventDto.getEventId();
        Optional<Event> optionalEvent =
                eventDao.get(eventId);
        Event eventToBeModified = optionalEvent
                .orElseThrow(() -> new EntityNotFoundException());
        eventToBeModified = entityDtoMapper
                .toEntity(eventDto, eventToBeModified);
        Optional<User> optionalUser = eventDao
                .getPrimaryParent(eventId);
        User user = optionalUser
                .orElseThrow(() -> new EntityNotFoundException());
        Long userId = user.getUserId();
        Event updatedEvent = eventDao
                .update(eventToBeModified,
                        userId);
        return entityDtoMapper
                .toDto(updatedEvent, userId);
    }
    @Override
    public EventDto addMember(EventMemberDto eventMemberDto) {
        Long eventId = eventMemberDto.getEventId();
        Long memberId = eventMemberDto.getMemberId();
        Event event = eventDao.addMember(eventId, memberId);
        Optional<User> optionalUser = eventDao.getPrimaryParent(eventId);
        User user = optionalUser
                .orElseThrow(() -> new EntityNotFoundException());
        Long userId = user.getUserId();
        return entityDtoMapper.toDto(
                event,
                userId);
    }
    @Override
    public void removeMember(Long eventId, Long memberId) {
        eventDao.removeMember(eventId, memberId);
    }
    @Override
    public void delete(Long eventId) {
        eventDao.delete(eventId);
    }
    @Inject
    public EventServiceImpl(EntityDtoMapper entityDtoMapper,
                            EventDao eventDao,
                            @ProcessAs(ProcessType.POLL)
                            ChildWithOneRequiredParentDao<Poll, Event> pollDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.eventDao = eventDao;
        this.pollDao = pollDao;
    }
    protected EventServiceImpl() {}
}
