package com.knotslicer.server.adapters.jpa;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.ScheduleDao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional(rollbackOn={Exception.class})
public class ScheduleDaoImpl implements ScheduleDao {

    @PersistenceContext(unitName = "knotslicer_database")
    private EntityManager entityManager;

    @Override
    public Schedule create(Schedule schedule, Long memberId) {
        MemberImpl memberImpl = getMemberWithSchedulesFromJpa(memberId);
        entityManager.detach(memberImpl);
        memberImpl.addSchedule((ScheduleImpl) schedule);
        memberImpl = entityManager.merge(memberImpl);
        entityManager.flush();
        schedule = getScheduleFromMember(memberImpl, schedule);
        entityManager.refresh(schedule);
        return schedule;
    }
    private MemberImpl getMemberWithSchedulesFromJpa(Long memberId) {
        TypedQuery<MemberImpl> query = entityManager.createQuery
                        ("SELECT m FROM Member m " +
                                "INNER JOIN FETCH m.schedules " +
                                "WHERE m.memberId = :memberId", MemberImpl.class)
                .setParameter("memberId", memberId);
        return query.getSingleResult();
    }
    private ScheduleImpl getScheduleFromMember(MemberImpl memberImpl, Schedule schedule) {
        List<ScheduleImpl> scheduleList = memberImpl.getSchedules();
        int scheduleIndex = scheduleList.indexOf(schedule);
        return scheduleList.get(scheduleIndex);
    }
    @Override
    public Optional<Schedule> get(Long scheduleId) {
        Schedule schedule = entityManager.find(ScheduleImpl.class, scheduleId);
        return Optional.ofNullable(schedule);
    }
    @Override
    public Optional<Member> getPrimaryParentWithChildren(Long memberId) {
        Member member = getMemberWithSchedulesFromJpa(memberId);
        return Optional.ofNullable(member);
    }
    @Override
    public Schedule update(Schedule inputSchedule, Long memberId) {
        MemberImpl memberImpl = getMemberWithSchedulesFromJpa(memberId);
        entityManager.detach(memberImpl);
        Schedule scheduleToBeModified =
                getScheduleFromMember(memberImpl, inputSchedule);
        scheduleToBeModified.setStartTimeUtc(
                inputSchedule.getStartTimeUtc());
        scheduleToBeModified.setEndTimeUtc(
                inputSchedule.getEndTimeUtc());
        memberImpl = entityManager
                .merge(memberImpl);
        entityManager.flush();
        Schedule updatedSchedule = getScheduleFromMember(memberImpl, scheduleToBeModified);
        return updatedSchedule;
    }
    @Override
    public void delete(Long scheduleId, Long memberId) {
        MemberImpl memberImpl = getMemberWithSchedulesFromJpa(memberId);
        ScheduleImpl scheduleImpl = entityManager.find(ScheduleImpl.class, scheduleId);
        memberImpl.removeSchedule(scheduleImpl);
        entityManager.flush();
    }
}
