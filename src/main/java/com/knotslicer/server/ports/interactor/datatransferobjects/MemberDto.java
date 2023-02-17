package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(as = MemberDtoImpl.class)
public interface MemberDto extends MemberLightDto {
    Long getProjectOwnerId();
    void setProjectOwnerId(Long projectOwnerId);
    List<ScheduleDto> getSchedules();
    void setSchedules(List<ScheduleDto> schedules);
}
