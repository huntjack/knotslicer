package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Schedule;
import com.knotslicer.server.ports.entitygateway.ScheduleDao;
import com.knotslicer.server.ports.interactor.datatransferobjects.ScheduleDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Map;
import java.util.Optional;

@ScheduleService
@ApplicationScoped
public class ScheduleServiceImpl implements Service<ScheduleDto> {
    @Inject
    EntityDtoMapper entityDtoMapper;
    @Inject
    ScheduleDao scheduleDao;

    @Override
    public ScheduleDto create(ScheduleDto scheduleDto) {
        Schedule schedule = entityDtoMapper
                .toEntity(scheduleDto);
        Long memberId = scheduleDto
                .getMemberId();
        schedule = scheduleDao.create(
                schedule,
                memberId);
        Long userId = scheduleDto.getUserId();
        return entityDtoMapper.toDto(
                schedule,
                memberId,
                userId);
    }

    @Override
    public ScheduleDto get(Map<String,Long> primaryKeys) {
        Long scheduleId = primaryKeys.get("scheduleId");
        Optional<Schedule> optionalSchedule = scheduleDao.get(scheduleId);
        Schedule schedule = unpackOptionalSchedule(optionalSchedule);
        Long memberId = primaryKeys.get("memberId");
        Long userId = primaryKeys.get("userId");
        return entityDtoMapper.toDto(schedule, memberId, userId);
    }
    private Schedule unpackOptionalSchedule(Optional<Schedule> optionalSchedule) {
        return optionalSchedule.orElseThrow(() -> new EntityNotFoundException("Schedule not found."));
    }

    @Override
    public ScheduleDto getWithChildren(Map<String,Long> primaryKeys) {
        return null;
    }

    @Override
    public ScheduleDto update(ScheduleDto scheduleDto) {
        return null;
    }

    @Override
    public void delete(Map<String,Long> primaryKeys) {
        Long scheduleId = primaryKeys.get("scheduleId");
        Long memberId = primaryKeys.get("memberId");
        scheduleDao.delete(scheduleId, memberId);
    }
}
