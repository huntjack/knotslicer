package com.knotslicer.server.ports.interactor.mappers;

import com.knotslicer.server.ports.interactor.datatransferobjects.*;
import com.knotslicer.server.domain.*;
import jakarta.validation.Valid;

public interface EntityDtoMapper {
    UserLightDto toDto(User userInput);
    UserLightDto addProjectDtosToUserLightDto(UserLightDto userLightDto, User userInput);
    UserLightDto addMemberDtosToUserLightDto(UserLightDto userLightDto, User userInput);
    UserLightDto addEventDtosToUserLightDto(UserLightDto userLightDto, User userInput);
    @Valid
    User toEntity(UserDto userDtoInput);
    @Valid
    User toEntity(UserLightDto userLightDtoInput, User userToBeModified);
    ProjectDto toDto(Project projectInput, Long userId);
    ProjectDto addMemberDtosToProjectDto(ProjectDto projectDto, Project projectInput);
    @Valid
    Project toEntity(ProjectDto projectDtoInput);
    @Valid
    Project toEntity(ProjectDto projectDtoInput, Project projectToBeModified);
    MemberDto toDto(Member memberInput, Long userId, Long projectId);
    MemberDto addScheduleDtosToMemberDto(MemberDto memberDto, Member memberInput);
    MemberDto addEventDtosToMemberDto(MemberDto memberDto, Member memberInput);
    @Valid
    Member toEntity(MemberDto memberDtoInput);
    @Valid
    Member toEntity(MemberDto memberDtoInput, Member memberToBeModified);

    EventDto toDto(Event eventInput);
    EventDto toDto(Event eventInput, Long userId);
    EventDto addPollDtosToEventDto(EventDto eventDto, Event eventInput);
    EventDto addMemberDtosToEventDto(EventDto eventDto, Event eventInput);
    @Valid
    Event toEntity(EventDto eventDtoInput);
    @Valid
    Event toEntity(EventDto eventDtoInput, Event eventToBeModified);

    ScheduleDto toDto(Schedule scheduleInput, Long memberId);
    @Valid
    Schedule toEntity(ScheduleDto scheduleDtoInput);
    @Valid
    Schedule toEntity(ScheduleDto scheduleDtoInput, Schedule scheduleToBeModified);

    PollDto toDto(Poll pollInput, Long eventId);
    PollDto addPollAnswerDtosToPollDto(PollDto pollDto, Poll pollInput);
    @Valid
    Poll toEntity(PollDto pollDtoInput);
    @Valid
    Poll toEntity(PollDto pollDtoInput, Poll pollToBeModified);

    PollAnswerDto toDto(PollAnswer pollAnswerInput, Long pollId, Long memberId);
    @Valid
    PollAnswer toEntity(PollAnswerDto pollAnswerDtoInput);
    @Valid
    PollAnswer toEntity(PollAnswerDto pollAnswerDtoInput, PollAnswer pollAnswerToBeModified);


}
