package com.knotslicer.server.control;

import com.knotslicer.server.entity.*;

public interface EntityMapper {
    User toDto(User userJpaEntityInput);
    UserLight toLightDto(User userJpaEntityInput);
    UserLight createUserLight();
    User toEntity(User userDtoEntityInput);
    User toEntity(User userDtoEntityInput, User userJpaEntityToBeModified);
    User toEntity(UserLight userLightDtoEntityInput, User userJpaEntityToBeModified);
    Project toDto(Project projectJpaEntityInput);
    Project toEntity(Project projectDtoEntityInput);
    Project toEntity(Project projectDtoEntityInput, Project projectJpaEntityToBeModified);

    Member toDto(Member memberJpaEntityInput);
    Member toEntity(Member memberDtoEntityInput);
    Member toEntity(Member memberDtoEntityInput, Member memberJpaEntityToBeModified);

    Event toDto(Event eventJpaEntityInput);
    Event toEntity(Event eventDtoEntityInput);
    Event toEntity(Event eventDtoEntityInput, Event eventJpaEntityToBeModified);

    Schedule toDto(Schedule scheduleJpaEntityInput);
    Schedule toEntity(Schedule scheduleDtoEntityInput);
    Schedule toEntity(Schedule scheduleDtoEntityInput, Schedule scheduleJpaEntityToBeModified);

    Poll toDto(Poll pollJpaEntityInput);
    Poll toEntity(Poll pollDtoEntityInput);
    Poll toEntity(Poll pollDtoEntityInput, Poll pollJpaEntityToBeModified);

    PollAnswer toDto(PollAnswer pollAnswerJpaEntityInput);
    PollAnswer toEntity(PollAnswer pollAnswerDtoEntityInput);
    PollAnswer toEntity(PollAnswer pollAnswerDtoEntityInput, PollAnswer pollAnswerJpaEntityToBeModified);


}
