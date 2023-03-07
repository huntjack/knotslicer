package com.knotslicer.server.ports.entitygateway;

import com.knotslicer.server.domain.Event;
import com.knotslicer.server.domain.User;

import java.util.Optional;

public interface EventDao extends ChildWithOneRequiredParentDao<Event, User> {
    Event addMember(Long eventId, Long memberId);
    Event getWithMembers(Long eventId);
    void removeMember(Long eventId, Long memberId);
}
