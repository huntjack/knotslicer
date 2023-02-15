package com.knotslicer.server.ports.interactor.mappers;

import com.knotslicer.server.ports.interactor.datatransferobjects.*;
import com.knotslicer.server.domain.*;

import java.util.Map;

public interface EntityDtoMapper {
    UserDto toDto(User userInput);
    UserLightDto toLightDto(User userInput);
    User toEntity(UserDto userDtoInput);
    User toEntity(UserLightDto userLightDtoInput, User userToBeModified);
    ProjectDto toDto(Project projectInput, Long userId);
    ProjectDto addMemberDtosToProjectDto(ProjectDto projectDto, Project projectInput);
    Project toEntity(ProjectDto projectDtoInput);
    Project toEntity(ProjectDto projectDtoInput, Project projectToBeModified);
    MemberLightDto toDto(Member memberInput, Long userId, Long projectId);
    MemberDto toDto(Member memberInput, Map<String,Long> primaryKeys);
    Member toEntity(MemberLightDto memberDtoInput);
    Member toEntity(MemberLightDto memberDtoInput, Member memberToBeModified);

    EventDto toDto(Event eventInput, Long userId);
    Event toEntity(EventDto eventDtoInput);
    Event toEntity(EventDto eventDtoInput, Event eventToBeModified);

    ScheduleDto toDto(Schedule scheduleInput, Map<String,Long> primaryKeys);
    Schedule toEntity(ScheduleDto scheduleDtoInput);
    Schedule toEntity(ScheduleDto scheduleDtoInput, Schedule scheduleToBeModified);

    PollDto toDto(Poll pollInput, Long eventId);
    Poll toEntity(PollDto pollDtoInput);
    Poll toEntity(PollDto pollDtoInput, Poll pollToBeModified);

    PollAnswerDto toDto(PollAnswer pollAnswerInput, Long pollId);
    PollAnswer toEntity(PollAnswerDto pollAnswerDtoInput);
    PollAnswer toEntity(PollAnswerDto pollAnswerDtoInput, PollAnswer pollAnswerToBeModified);


}
