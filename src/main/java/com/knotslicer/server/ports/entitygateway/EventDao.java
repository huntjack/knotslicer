package com.knotslicer.server.ports.entitygateway;

import com.knotslicer.server.domain.Event;
import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.User;

import java.util.Optional;

public interface EventDao extends ChildWithOneParentDao<Event, User> {
    Optional<Member> getMemberWithSchedules(Long memberId);
}
