package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Event;
import com.knotslicer.server.ports.entitygateway.EventDao;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@EventService
@ApplicationScoped
public class EventServiceImpl implements ParentService<EventDto> {
    private EntityDtoMapper entityDtoMapper;
    private EventDao eventDao;

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
        Event event = unpackOptionalEvent(optionalEvent);
        Long userId = eventDao.getPrimaryParentId(eventId);
        return entityDtoMapper
                .toDto(event, userId);
    }
    private Event unpackOptionalEvent(Optional<Event> optionalEvent) {
        return optionalEvent.orElseThrow(() -> new EntityNotFoundException("Event not found."));
    }
    @Override
    public EventDto getWithChildren(Long eventId) {
        return null;
    }
    @Override
    public EventDto update(EventDto eventDto) {
        Long eventId = eventDto.getEventId();
        Optional<Event> optionalEvent =
                eventDao.get(eventId);
        Event eventToBeModified = unpackOptionalEvent(optionalEvent);
        eventToBeModified = entityDtoMapper
                .toEntity(eventDto, eventToBeModified);
        Long userId = eventDao
                .getPrimaryParentId(eventId);
        Event updatedEvent = eventDao
                .update(eventToBeModified,
                        userId);
        return entityDtoMapper
                .toDto(updatedEvent, userId);
    }
    @Override
    public void delete(Long eventId) {
        Long userId = eventDao.getPrimaryParentId(eventId);
        eventDao.delete(eventId, userId);
    }
    @Inject
    public EventServiceImpl(EntityDtoMapper entityDtoMapper, EventDao eventDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.eventDao = eventDao;
    }
    protected EventServiceImpl() {}
}
