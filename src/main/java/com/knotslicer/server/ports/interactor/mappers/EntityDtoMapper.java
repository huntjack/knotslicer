package com.knotslicer.server.ports.interactor.mappers;

import com.knotslicer.server.ports.interactor.datatransferobjects.*;
import com.knotslicer.server.domain.*;

public interface EntityDtoMapper {
    UserDto toDto(User userInput);
    UserLightDto toLightDto(User userInput);
    User toEntity(UserDto userDtoInput);
    User toEntity(UserDto userDtoInput, User userToBeModified);
    User toEntity(UserLightDto userLightDtoInput, User userToBeModified);
    ProjectDto toDto(Project projectInput);
    ProjectDto toDto(Project projectInput, Long userId);
    ProjectDto addMembers(ProjectDto projectDto, Project projectInput);
    Project toEntity(ProjectDto projectDtoInput);
    Project toEntity(ProjectDto projectDtoInput, Project projectToBeModified);
    MemberDto toDto(Member memberInput);
    MemberDto toDto(Member memberInput, Long userId, Long projectId);
    Member toEntity(MemberDto memberDtoInput);
    Member toEntity(MemberDto memberDtoInput, Member memberToBeModified);

    EventDto toDto(Event eventInput, Long userId);
    Event toEntity(EventDto eventDtoInput);
    Event toEntity(EventDto eventDtoInput, Event eventToBeModified);

    ScheduleDto toDto(Schedule scheduleInput, Long memberId);
    Schedule toEntity(ScheduleDto scheduleDtoInput);
    Schedule toEntity(ScheduleDto scheduleDtoInput, Schedule scheduleToBeModified);

    PollDto toDto(Poll pollInput, Long eventId);
    Poll toEntity(PollDto pollDtoInput);
    Poll toEntity(PollDto pollDtoInput, Poll pollToBeModified);

    PollAnswerDto toDto(PollAnswer pollAnswerInput, Long pollId);
    PollAnswer toEntity(PollAnswerDto pollAnswerDtoInput);
    PollAnswer toEntity(PollAnswerDto pollAnswerDtoInput, PollAnswer pollAnswerToBeModified);


}
