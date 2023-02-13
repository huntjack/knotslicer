package com.knotslicer.server.ports.interactor.datatransferobjects;

public interface DtoCreator {
    UserDto createUserDto();
    UserLightDto createUserLightDto();
    ProjectDto createProjectDto();
    MemberDto createMemberDto();
    MemberLightDto createMemberLightDto();
    EventDto createEventDto();
    ScheduleDto createScheduleDto();
    PollDto createPollDto();
    PollAnswerDto createPollAnswerDto();
}
