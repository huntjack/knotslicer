package com.knotslicer.server.ports.interactor.datatransferobjects;

public interface DtoFactory {
    UserDto createUserDto();
    UserLightDto createUserLightDto();
    ProjectDto createProjectDto();
    MemberDto createMemberDto();
    EventDto createEventDto();
    ScheduleDto createScheduleDto();
    PollDto createPollDto();
    PollAnswerDto createPollAnswerDto();
}
