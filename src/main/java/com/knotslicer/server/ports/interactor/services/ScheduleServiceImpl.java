package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Schedule;
import com.knotslicer.server.ports.entitygateway.ScheduleDao;
import com.knotslicer.server.ports.interactor.datatransferobjects.ScheduleDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.HashMap;
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
        Long memberId = scheduleDto.getMemberId();
        schedule = scheduleDao.create(
                schedule,
                memberId);
        Long userId = scheduleDto.getUserId();
        Map<String,Long> ids = packIds(memberId, userId);
        return entityDtoMapper.toDto(
                schedule,
                ids);
    }
    private Map<String,Long> packIds(Long memberId, Long userId) {
        Map<String,Long> ids = new HashMap<>(3);
        ids.put("memberId", memberId);
        ids.put("userId", userId);
        return ids;
    }
    @Override
    public ScheduleDto get(Map<String,Long> ids) {
        Long scheduleId = ids.get("scheduleId");
        Optional<Schedule> optionalSchedule = scheduleDao.get(scheduleId);
        Schedule schedule = unpackOptionalSchedule(optionalSchedule);
        return entityDtoMapper.toDto(schedule, ids);
    }
    private Schedule unpackOptionalSchedule(Optional<Schedule> optionalSchedule) {
        return optionalSchedule.orElseThrow(() -> new EntityNotFoundException("Schedule not found."));
    }

    @Override
    public ScheduleDto update(ScheduleDto scheduleDto) {
        Long scheduleId = scheduleDto.getScheduleId();
        Optional<Schedule> optionalSchedule =
                scheduleDao.get(scheduleId);
        Schedule scheduleToBeModified = unpackOptionalSchedule(optionalSchedule);

        scheduleToBeModified = entityDtoMapper.toEntity(
                        scheduleDto,
                        scheduleToBeModified);
        Long memberId = scheduleDto.getMemberId();
        Schedule updatedSchedule = scheduleDao.update(
                scheduleToBeModified,
                memberId);

        Long userId = scheduleDto.getUserId();
        Map<String,Long> ids = packIds(memberId, userId);
        return entityDtoMapper.toDto(
                updatedSchedule,
                ids);
    }

    @Override
    public void delete(Map<String,Long> ids) {
        Long scheduleId = ids
                .get("scheduleId");
        Long memberId = ids
                .get("memberId");
        scheduleDao.delete(
                scheduleId,
                memberId);
    }
}
