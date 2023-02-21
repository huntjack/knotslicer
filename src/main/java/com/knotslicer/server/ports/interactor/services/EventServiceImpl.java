package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Event;
import com.knotslicer.server.ports.entitygateway.EventDao;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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
        return entityDtoMapper.toDto(
                event,
                userId);
    }
    @Override
    public EventDto get(Long id) {
        return null;
    }
    @Override
    public EventDto getWithChildren(Long id) {
        return null;
    }
    @Override
    public EventDto update(EventDto eventDto) {
        return null;
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
