package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(as = EventDtoImpl.class)
public interface EventDto extends Linkable {
    Long getUserId();
    void setUserId(Long userId);
    Long getEventId();
    void setEventId(Long eventId);
    String getSubject();
    void setSubject(String subject);
    String getEventName();
    void setEventName(String eventName);
    String getEventDescription();
    void setEventDescription(String eventDescription);
    List<MemberDto> getMembers();
    void setMembers(List<MemberDto> members);
    List<PollDto> getPolls();
    void setPolls(List<PollDto> pollDtos);
}
