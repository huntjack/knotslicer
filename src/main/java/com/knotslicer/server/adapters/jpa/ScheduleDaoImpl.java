package com.knotslicer.server.adapters.jpa;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
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
        Optional<Member> optionalMemberWithSchedules = getPrimaryParentWithChildren(memberId);
        MemberImpl memberWithSchedules = (MemberImpl) optionalMemberWithSchedules
                .orElseThrow(() -> new EntityNotFoundException());
        entityManager.detach(memberWithSchedules);
        memberWithSchedules.addSchedule((ScheduleImpl) schedule);
        memberWithSchedules = entityManager.merge(memberWithSchedules);
        entityManager.flush();
        Optional<Schedule> optionalSchedule =
                getScheduleFromMember(
                        memberWithSchedules,
                        schedule);
        return optionalSchedule
                .orElseThrow(() -> new EntityNotFoundException());
    }
    @Override
    public Optional<Member> getPrimaryParentWithChildren(Long memberId) {
        TypedQuery<MemberImpl> query = entityManager.createQuery
                        ("SELECT m FROM Member m " +
                                "LEFT JOIN FETCH m.schedules " +
                                "WHERE m.memberId = :memberId", MemberImpl.class)
                .setParameter("memberId", memberId);
        Member member = query.getSingleResult();
        return Optional.ofNullable(member);
    }
    private Optional<Schedule> getScheduleFromMember(MemberImpl memberImpl, Schedule schedule) {
        List<ScheduleImpl> scheduleImpls = memberImpl.getSchedules();
        int scheduleIndex = scheduleImpls.indexOf(schedule);
        schedule = scheduleImpls.get(scheduleIndex);
        return Optional.ofNullable(schedule);
    }
    @Override
    public Optional<Schedule> get(Long scheduleId) {
        Schedule schedule = entityManager.find(ScheduleImpl.class, scheduleId);
        return Optional.ofNullable(schedule);
    }
    @Override
    public Optional<Member> getPrimaryParent(Long scheduleId) {
        TypedQuery<MemberImpl> query = entityManager.createQuery(
                "SELECT m FROM Member m " +
                        "INNER JOIN m.schedules schedule " +
                        "WHERE schedule.scheduleId = :scheduleId", MemberImpl.class)
                .setParameter("scheduleId", scheduleId);
        Member member = query.getSingleResult();
        return Optional.ofNullable(member);
    }
    @Override
    public Schedule update(Schedule scheduleInput, Long memberId) {
        Optional<Member> optionalMemberWithSchedules = getPrimaryParentWithChildren(memberId);
        MemberImpl memberWithSchedules = (MemberImpl) optionalMemberWithSchedules
                .orElseThrow(() -> new EntityNotFoundException());
        Optional<Schedule> optionalScheduleToBeModified =
                getScheduleFromMember(memberWithSchedules, scheduleInput);
        Schedule scheduleToBeModified = optionalScheduleToBeModified
                .orElseThrow(() -> new EntityNotFoundException());
        entityManager.detach(memberWithSchedules);
        scheduleToBeModified.setStartTimeUtc(
                scheduleInput.getStartTimeUtc());
        scheduleToBeModified.setEndTimeUtc(
                scheduleInput.getEndTimeUtc());
        memberWithSchedules = entityManager
                .merge(memberWithSchedules);
        entityManager.flush();
        Optional<Schedule> optionalScheduleResponse =
                getScheduleFromMember(
                        memberWithSchedules,
                        scheduleToBeModified);
        return optionalScheduleResponse
                .orElseThrow(() -> new EntityNotFoundException());
    }
    @Override
    public void delete(Long scheduleId) {
        Optional<Member> optionalMember = getPrimaryParent(scheduleId);
        Member member = optionalMember
                .orElseThrow(() -> new EntityNotFoundException());
        Optional<Member> optionalMemberWithSchedules =
                getPrimaryParentWithChildren(
                        member.getMemberId());
        MemberImpl memberWithSchedules = (MemberImpl) optionalMemberWithSchedules
                .orElseThrow(() -> new EntityNotFoundException());
        Optional<Schedule> optionalSchedule = get(scheduleId);
        ScheduleImpl scheduleImpl = (ScheduleImpl) optionalSchedule
                .orElseThrow(() -> new EntityNotFoundException());
        memberWithSchedules.removeSchedule(scheduleImpl);
        entityManager.flush();
    }
}
