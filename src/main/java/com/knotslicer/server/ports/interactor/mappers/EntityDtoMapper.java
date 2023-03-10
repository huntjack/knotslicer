package com.knotslicer.server.ports.interactor.mappers;

import com.knotslicer.server.ports.interactor.datatransferobjects.*;
import com.knotslicer.server.domain.*;

public interface EntityDtoMapper {
    UserLightDto toDto(User userInput);
    UserLightDto addProjectDtosToUserLightDto(UserLightDto userLightDto, User userInput);
    UserLightDto addMemberDtosToUserLightDto(UserLightDto userLightDto, User userInput);
    UserLightDto addEventDtosToUserLightDto(UserLightDto userLightDto, User userInput);
    User toEntity(UserDto userDtoInput);
    User toEntity(UserLightDto userLightDtoInput, User userToBeModified);
    ProjectDto toDto(Project projectInput, Long userId);
    ProjectDto addMemberDtosToProjectDto(ProjectDto projectDto, Project projectInput);
    Project toEntity(ProjectDto projectDtoInput);
    Project toEntity(ProjectDto projectDtoInput, Project projectToBeModified);
    MemberDto toDto(Member memberInput, Long userId, Long projectId);
    MemberDto addScheduleDtosToMemberDto(MemberDto memberDto, Member memberInput);
    Member toEntity(MemberDto memberDtoInput);
    Member toEntity(MemberDto memberDtoInput, Member memberToBeModified);

    EventDto toDto(Event eventInput, Long userId);
    EventDto addPollDtosToEventDto(EventDto eventDto, Event eventInput);
    EventDto addMemberDtosToEventDto(EventDto eventDto, Event eventInput);
    Event toEntity(EventDto eventDtoInput);
    Event toEntity(EventDto eventDtoInput, Event eventToBeModified);

    ScheduleDto toDto(Schedule scheduleInput, Long memberId);
    Schedule toEntity(ScheduleDto scheduleDtoInput);
    Schedule toEntity(ScheduleDto scheduleDtoInput, Schedule scheduleToBeModified);

    PollDto toDto(Poll pollInput, Long eventId);
    PollDto addPollAnswerDtosToPollDto(PollDto pollDto, Poll pollInput);
    Poll toEntity(PollDto pollDtoInput);
    Poll toEntity(PollDto pollDtoInput, Poll pollToBeModified);

    PollAnswerDto toDto(PollAnswer pollAnswerInput, Long pollId, Long memberId);
    PollAnswer toEntity(PollAnswerDto pollAnswerDtoInput);
    PollAnswer toEntity(PollAnswerDto pollAnswerDtoInput, PollAnswer pollAnswerToBeModified);


}
