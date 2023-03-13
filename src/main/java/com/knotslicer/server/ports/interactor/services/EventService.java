package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventMemberDto;

public interface EventService extends ParentService<EventDto> {
    void addMember(EventMemberDto eventMemberDto);
    EventDto getWithMembers(Long eventId);
    void removeMember(Long eventId, Long memberId);
}
