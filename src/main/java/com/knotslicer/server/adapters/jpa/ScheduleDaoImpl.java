package com.knotslicer.server.adapters.jpa;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ProcessAs(ProcessType.SCHEDULE)
@ApplicationScoped
@Transactional(rollbackOn={Exception.class})
public class ScheduleDaoImpl implements ChildWithOneRequiredParentDao<Schedule, Member> {

    @PersistenceContext(unitName = "knotslicer_database")
    private EntityManager entityManager;

    @Override
    public Schedule create(Schedule schedule, Long memberId) {
        MemberImpl memberImpl = getMemberWithSchedulesFromJpa(memberId);
        entityManager.detach(memberImpl);
        memberImpl.addSchedule((ScheduleImpl) schedule);
        memberImpl = entityManager.merge(memberImpl);
        entityManager.flush();
        return getScheduleFromMember(
                memberImpl,
                schedule);
    }
    private MemberImpl getMemberWithSchedulesFromJpa(Long memberId) {
        TypedQuery<MemberImpl> query = entityManager.createQuery
                        ("SELECT m FROM Member m " +
                                "LEFT JOIN FETCH m.schedules " +
                                "WHERE m.memberId = :memberId", MemberImpl.class)
                .setParameter("memberId", memberId);
        return query.getSingleResult();
    }
    private Schedule getScheduleFromMember(MemberImpl memberImpl, Schedule schedule) {
        List<ScheduleImpl> scheduleImpls = memberImpl.getSchedules();
        int scheduleIndex = scheduleImpls.indexOf(schedule);
        return scheduleImpls.get(scheduleIndex);
    }
    @Override
    public Optional<Schedule> get(Long scheduleId) {
        Schedule schedule = entityManager.find(ScheduleImpl.class, scheduleId);
        return Optional.ofNullable(schedule);
    }
    @Override
    public Member getPrimaryParent(Long scheduleId) {
        TypedQuery<MemberImpl> query = entityManager.createQuery(
                "SELECT m FROM Member m " +
                        "INNER JOIN m.schedules schedule " +
                        "WHERE schedule.scheduleId = :scheduleId", MemberImpl.class)
                .setParameter("scheduleId", scheduleId);
        return query.getSingleResult();
    }
    @Override
    public Optional<Member> getPrimaryParentWithChildren(Long memberId) {
        Member member = getMemberWithSchedulesFromJpa(memberId);
        return Optional.ofNullable(member);
    }
    @Override
    public Schedule update(Schedule scheduleInput, Long memberId) {
        MemberImpl memberImpl = getMemberWithSchedulesFromJpa(memberId);
        entityManager.detach(memberImpl);
        Schedule scheduleToBeModified =
                getScheduleFromMember(memberImpl, scheduleInput);
        scheduleToBeModified.setStartTimeUtc(
                scheduleInput.getStartTimeUtc());
        scheduleToBeModified.setEndTimeUtc(
                scheduleInput.getEndTimeUtc());
        memberImpl = entityManager
                .merge(memberImpl);
        entityManager.flush();
        return getScheduleFromMember(
                memberImpl,
                scheduleToBeModified);
    }
    @Override
    public void delete(Long scheduleId) {
        Member member = getPrimaryParent(scheduleId);
        MemberImpl memberWithSchedules =
                getMemberWithSchedulesFromJpa(
                        member.getMemberId());
        ScheduleImpl scheduleImpl = entityManager.find(ScheduleImpl.class, scheduleId);
        memberWithSchedules.removeSchedule(scheduleImpl);
        entityManager.flush();
    }
}
