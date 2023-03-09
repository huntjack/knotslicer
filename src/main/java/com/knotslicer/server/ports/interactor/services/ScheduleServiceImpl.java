package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Schedule;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.ScheduleDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ProcessAs(ProcessType.SCHEDULE)
@ApplicationScoped
public class ScheduleServiceImpl implements Service<ScheduleDto> {
    private EntityDtoMapper entityDtoMapper;
    private ChildWithOneRequiredParentDao<Schedule, Member> scheduleDao;

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
        Schedule schedule = optionalSchedule
                .orElseThrow(() -> new EntityNotFoundException());
        Optional<Member> optionalMember = scheduleDao.getPrimaryParent(scheduleId);
        Member member = optionalMember
                .orElseThrow(() -> new EntityNotFoundException());
        Long memberId = member.getMemberId();
        return entityDtoMapper.toDto(
                schedule,
                memberId);
    }
    @Override
    public ScheduleDto update(ScheduleDto scheduleDto) {
        Long scheduleId = scheduleDto.getScheduleId();
        Optional<Schedule> optionalSchedule =
                scheduleDao.get(scheduleId);
        Schedule scheduleToBeModified = optionalSchedule
                .orElseThrow(() -> new EntityNotFoundException());

        scheduleToBeModified = entityDtoMapper.toEntity(
                        scheduleDto,
                        scheduleToBeModified);
        Long memberId = scheduleDto.getMemberId();
        Schedule updatedSchedule = scheduleDao.update(
                scheduleToBeModified,
                memberId);

        return entityDtoMapper.toDto(
                updatedSchedule,
                memberId);
    }
    @Override
    public void delete(Long scheduleId) {
        scheduleDao.delete(scheduleId);
    }
    @Inject
    public ScheduleServiceImpl(EntityDtoMapper entityDtoMapper,
                               @ProcessAs(ProcessType.SCHEDULE)
                               ChildWithOneRequiredParentDao<Schedule, Member> scheduleDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.scheduleDao = scheduleDao;
    }
    protected ScheduleServiceImpl() {}
}
