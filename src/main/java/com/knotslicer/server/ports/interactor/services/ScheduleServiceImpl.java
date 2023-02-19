package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Schedule;
import com.knotslicer.server.ports.entitygateway.ScheduleDao;
import com.knotslicer.server.ports.interactor.datatransferobjects.ScheduleDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ScheduleService
@ApplicationScoped
public class ScheduleServiceImpl implements Service<ScheduleDto> {
    private EntityDtoMapper entityDtoMapper;
    private ScheduleDao scheduleDao;

    @Override
    public ScheduleDto create(ScheduleDto scheduleDto) {
        Schedule schedule = entityDtoMapper
                .toEntity(scheduleDto);
        Long memberId = scheduleDto.getMemberId();
        schedule = scheduleDao.create(
                schedule,
                memberId);
        return entityDtoMapper.toDto(
                schedule,
                memberId);
    }
    @Override
    public ScheduleDto get(Long scheduleId) {
        Optional<Schedule> optionalSchedule = scheduleDao.get(scheduleId);
        Schedule schedule = unpackOptionalSchedule(optionalSchedule);
        Long memberId = scheduleDao.getPrimaryParentId(scheduleId);
        return entityDtoMapper.toDto(
                schedule,
                memberId);
    }
    private Schedule unpackOptionalSchedule(Optional<Schedule> optionalSchedule) {
        return optionalSchedule.orElseThrow(() -> new EntityNotFoundException("Schedule not found."));
    }
    @Override
    public ScheduleDto update(ScheduleDto scheduleDto) {
        Long scheduleId = scheduleDto.getScheduleId();
        Long memberId = scheduleDto.getMemberId();
        Optional<Schedule> optionalSchedule =
                scheduleDao.get(scheduleId);
        Schedule scheduleToBeModified = unpackOptionalSchedule(optionalSchedule);

        scheduleToBeModified = entityDtoMapper.toEntity(
                        scheduleDto,
                        scheduleToBeModified);
        Schedule updatedSchedule = scheduleDao.update(
                scheduleToBeModified,
                memberId);

        return entityDtoMapper.toDto(
                updatedSchedule,
                memberId);
    }
    @Override
    public void delete(Long scheduleId) {
        Long memberId = scheduleDao.getPrimaryParentId(scheduleId);
        scheduleDao.delete(
                scheduleId,
                memberId);
    }
    @Inject
    public ScheduleServiceImpl(EntityDtoMapper entityDtoMapper, ScheduleDao scheduleDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.scheduleDao = scheduleDao;
    }
    protected ScheduleServiceImpl() {}
}
