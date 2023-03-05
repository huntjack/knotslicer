package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.time.ZoneId;
import java.util.List;

@JsonDeserialize(as = UserLightDtoImpl.class)
public interface UserLightDto extends Linkable {
    Long getUserId();
    void setUserId(Long userId);
    String getUserName();
    void setUserName(String userName);
    String getUserDescription();
    void setUserDescription(String userDescription);
    ZoneId getTimeZone();
    void setTimeZone(ZoneId timeZone);
    List<ProjectDto> getProjects();
    public void setProjects(List<ProjectDto> projects);
    public List<MemberDto> getMembers();
    public void setMembers(List<MemberDto> members);
    public List<EventDto> getEvents();
    public void setEvents(List<EventDto> events);
}
