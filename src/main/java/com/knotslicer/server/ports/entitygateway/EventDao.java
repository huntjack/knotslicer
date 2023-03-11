package com.knotslicer.server.ports.entitygateway;

import com.knotslicer.server.domain.Event;
import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.User;

import java.util.Optional;

public interface EventDao extends ChildWithOneRequiredParentDao<Event, User> {
    Event addMember(Long eventId, Long memberId);
    Boolean eventContainsMember(Long eventId, Long memberId);
    Optional<Event> getEventWithMembers(Long eventId);
    Optional<Member> getMemberWithEvents(Long memberId);
    void removeMember(Long eventId, Long memberId);

}
