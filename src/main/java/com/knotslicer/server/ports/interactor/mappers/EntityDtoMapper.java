package com.knotslicer.server.ports.interactor.mappers;

import com.knotslicer.server.ports.interactor.datatransferobjects.*;
import com.knotslicer.server.domain.*;

public interface EntityDtoMapper {
    UserDto toDto(User userInput);
    UserLightDto toLightDto(User userInput);
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
    Event toEntity(EventDto eventDtoInput);
    Event toEntity(EventDto eventDtoInput, Event eventToBeModified);
    EventDto addPollDtosToEventDto(EventDto eventDto, Event eventInput);

    ScheduleDto toDto(Schedule scheduleInput, Long memberId);
    Schedule toEntity(ScheduleDto scheduleDtoInput);
    Schedule toEntity(ScheduleDto scheduleDtoInput, Schedule scheduleToBeModified);

    PollDto toDto(Poll pollInput, Long eventId);
    Poll toEntity(PollDto pollDtoInput);
    Poll toEntity(PollDto pollDtoInput, Poll pollToBeModified);
    PollDto addPollAnswerDtosToPollDto(PollDto pollDto, Poll pollInput);

    PollAnswerDto toDto(PollAnswer pollAnswerInput, Long pollId, Long memberId);
    PollAnswer toEntity(PollAnswerDto pollAnswerDtoInput);
    PollAnswer toEntity(PollAnswerDto pollAnswerDtoInput, PollAnswer pollAnswerToBeModified);


}
