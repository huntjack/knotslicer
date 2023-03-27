package com.knotslicer.server.ports.entitygateway;

import com.knotslicer.server.domain.Event;
import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Schedule;
import com.knotslicer.server.domain.User;
import java.util.Optional;
import java.util.Set;

public interface EventDao extends ChildWithOneRequiredParentDao<Event, User> {
    Event addMember(Long eventId, Long memberId);
    Boolean eventContainsMember(Long eventId, Long memberId);
    Optional<Event> getEventWithMembers(Long eventId);
    Optional<Member> getMemberWithEvents(Long memberId);
    Optional<Set<Schedule>> getSchedulesOfAllMembersAttendingEvent(Long eventId);
    void removeMember(Long eventId, Long memberId);

}
