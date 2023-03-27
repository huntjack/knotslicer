package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventMemberDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollDto;

import java.util.List;
import java.util.Set;

public interface EventService extends ParentService<EventDto> {
    EventDto getWithMembers(Long eventId);
    void addMember(EventMemberDto eventMemberDto);
    void removeMember(Long eventId, Long memberId);
    List<PollDto> findAvailableEventTimes(Long eventId);
}
