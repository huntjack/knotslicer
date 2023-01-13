package com.knotslicer.server.control;

import com.knotslicer.server.entity.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class EntityMapperImpl implements EntityMapper {
    @Inject
    @DtoEntity
    AbstractEntityFactory dtoEntityFactory;
    @Inject
    @JpaEntity
    AbstractEntityFactory jpaEntityFactory;
    @Override
    public User toDto(User userJpaEntityInput) {
        User userDto = dtoEntityFactory.createUser();
        userDto.setEmail(userJpaEntityInput.getEmail());
        userDto.setUserName(userJpaEntityInput.getUserName());
        userDto.setUserDescription(userJpaEntityInput.getUserDescription());
        return userDto;
    }
    @Override
    public UserLight toLightDto(User userJpaEntityInput) {
        UserLight userLightDto = createUserLight();
        userLightDto.setUserName(userJpaEntityInput.getUserName());
        userLightDto.setUserDescription(userJpaEntityInput.getUserDescription());
        return userLightDto;
    }
    @Override
    public UserLight createUserLight() {
        return new UserLightDtoImpl();
    }
    @Override
    public User toEntity(User userDtoEntityInput) {
        User userJpaEntity = jpaEntityFactory.createUser();
        return setJpaEntityVariables(userJpaEntity, userDtoEntityInput);
    }
    private User setJpaEntityVariables(User userJpaEntity, User userDtoEntityInput) {
        userJpaEntity.setEmail(userDtoEntityInput.getEmail());
        userJpaEntity.setUserName(userDtoEntityInput.getUserName());
        userJpaEntity.setUserDescription(userDtoEntityInput.getUserDescription());
        return userJpaEntity;
    }
    @Override
    public User toEntity(User userDtoEntityInput, User userJpaEntityToBeModified) {
        return setJpaEntityVariables(userJpaEntityToBeModified, userDtoEntityInput);
    }
    @Override
    public User toEntity(UserLight userLightDtoEntityInput, User userJpaEntityToBeModified) {
        userJpaEntityToBeModified.setUserName(userLightDtoEntityInput.getUserName());
        userJpaEntityToBeModified.setUserDescription(userLightDtoEntityInput.getUserDescription());
        return userJpaEntityToBeModified;
    }
    @Override
    public Project toDto(Project projectJpaEntityInput) {
        Project projectDto = dtoEntityFactory.createProject();
        projectDto.setProjectName(projectJpaEntityInput.getProjectName());
        projectDto.setProjectDescription(projectJpaEntityInput.getProjectDescription());
        return projectDto;
    }
    @Override
    public Project toEntity(Project projectDtoEntityInput) {
        Project projectJpaEntity = jpaEntityFactory.createProject();
        return setJpaEntityVariables(projectJpaEntity, projectDtoEntityInput);
    }
    private Project setJpaEntityVariables(Project projectJpaEntity, Project projectDtoEntityInput) {
        projectJpaEntity.setProjectName(projectDtoEntityInput.getProjectName());
        projectJpaEntity.setProjectDescription(projectDtoEntityInput.getProjectDescription());
        return projectJpaEntity;
    }
    @Override
    public Project toEntity(Project projectDtoEntityInput, Project projectJpaEntityToBeModified) {
        return setJpaEntityVariables(projectJpaEntityToBeModified, projectDtoEntityInput);
    }
    @Override
    public Member toDto(Member memberJpaEntityInput) {
        Member memberDto = dtoEntityFactory.createMember();
        memberDto.setName(memberJpaEntityInput.getName());
        memberDto.setRole(memberJpaEntityInput.getRole());
        memberDto.setRoleDescription(memberJpaEntityInput.getRoleDescription());
        memberDto.setTimeZone(memberJpaEntityInput.getTimeZone());
        return memberDto;
    }
    @Override
    public Member toEntity(Member memberDtoEntityInput) {
        Member memberJpaEntity = jpaEntityFactory.createMember();
        return setJpaEntityVariables(memberJpaEntity, memberDtoEntityInput);
    }
    private Member setJpaEntityVariables(Member memberJpaEntity, Member memberDtoEntityInput) {
        memberJpaEntity.setName(memberDtoEntityInput.getName());
        memberJpaEntity.setRole(memberDtoEntityInput.getRole());
        memberJpaEntity.setRoleDescription(memberDtoEntityInput.getRoleDescription());
        memberJpaEntity.setTimeZone(memberDtoEntityInput.getTimeZone());
        return memberJpaEntity;
    }
    @Override
    public Member toEntity(Member memberDtoEntityInput, Member memberJpaEntityToBeModified) {
        return setJpaEntityVariables(memberJpaEntityToBeModified, memberDtoEntityInput);
    }
    @Override
    public Event toDto(Event eventJpaEntityInput) {
        Event eventDto = dtoEntityFactory.createEvent();
        eventDto.setEventName(eventJpaEntityInput.getEventName());
        eventDto.setSubject(eventJpaEntityInput.getSubject());
        eventDto.setEventDescription(eventJpaEntityInput.getEventDescription());
        return eventDto;
    }
    @Override
    public Event toEntity(Event eventDtoEntityInput) {
        Event eventJpaEntity = jpaEntityFactory.createEvent();
        return setJpaEntityVariables(eventJpaEntity, eventDtoEntityInput);
    }
    private Event setJpaEntityVariables(Event eventJpaEntity, Event eventDtoEntityInput) {
        eventJpaEntity.setEventName(eventDtoEntityInput.getEventName());
        eventJpaEntity.setSubject(eventDtoEntityInput.getSubject());
        eventJpaEntity.setEventDescription(eventDtoEntityInput.getEventDescription());
        return eventJpaEntity;
    }
    @Override
    public Event toEntity(Event eventDtoEntityInput, Event eventJpaEntityToBeModified) {
        return setJpaEntityVariables(eventJpaEntityToBeModified, eventDtoEntityInput);
    }
    @Override
    public Schedule toDto(Schedule scheduleJpaEntityInput) {
        Schedule scheduleDto = dtoEntityFactory.createSchedule();
        scheduleDto.setStartTimeUtc(scheduleJpaEntityInput.getStartTimeUtc());
        scheduleDto.setEndTimeUtc(scheduleJpaEntityInput.getEndTimeUtc());
        return scheduleDto;
    }
    @Override
    public Schedule toEntity(Schedule scheduleDtoEntityInput) {
        Schedule scheduleJpaEntity = jpaEntityFactory.createSchedule();
        return setJpaEntityVariables(scheduleJpaEntity, scheduleDtoEntityInput);
    }
    private Schedule setJpaEntityVariables(Schedule scheduleJpaEntity, Schedule scheduleDtoEntityInput) {
        scheduleJpaEntity.setStartTimeUtc(scheduleDtoEntityInput.getStartTimeUtc());
        scheduleJpaEntity.setEndTimeUtc(scheduleDtoEntityInput.getEndTimeUtc());
        return scheduleJpaEntity;
    }
    @Override
    public Schedule toEntity(Schedule scheduleDtoEntityInput, Schedule scheduleJpaEntityToBeModified) {
        return setJpaEntityVariables(scheduleJpaEntityToBeModified, scheduleDtoEntityInput);
    }
    @Override
    public Poll toDto(Poll pollJpaEntityInput) {
        Poll pollDto = dtoEntityFactory.createPoll();
        pollDto.setStartTimeUtc(pollJpaEntityInput.getStartTimeUtc());
        pollDto.setEndTimeUtc(pollJpaEntityInput.getEndTimeUtc());
        return pollDto;
    }
    @Override
    public Poll toEntity(Poll pollDtoEntityInput) {
        Poll pollJpaEntity = jpaEntityFactory.createPoll();
        return setJpaEntityVariables(pollJpaEntity, pollDtoEntityInput);
    }
    private Poll setJpaEntityVariables(Poll pollJpaEntity, Poll pollDtoEntityInput) {
        pollJpaEntity.setStartTimeUtc(pollDtoEntityInput.getStartTimeUtc());
        pollJpaEntity.setEndTimeUtc(pollDtoEntityInput.getEndTimeUtc());
        return pollJpaEntity;
    }
    @Override
    public Poll toEntity(Poll pollDtoEntityInput, Poll pollJpaEntityToBeModified) {
        return setJpaEntityVariables(pollJpaEntityToBeModified, pollDtoEntityInput);
    }
    @Override
    public PollAnswer toDto(PollAnswer pollAnswerJpaEntityInput) {
        PollAnswer pollAnswerDto = dtoEntityFactory.createPollAnswer();
        pollAnswerDto.setApproved(pollAnswerJpaEntityInput.isApproved());
        return pollAnswerDto;
    }
    @Override
    public PollAnswer toEntity(PollAnswer pollAnswerDtoEntityInput) {
        PollAnswer pollAnswerJpaEntity = jpaEntityFactory.createPollAnswer();
        return setJpaEntityVariables(pollAnswerJpaEntity, pollAnswerDtoEntityInput);
    }
    private PollAnswer setJpaEntityVariables(PollAnswer pollAnswerJpaEntity, PollAnswer pollAnswerDtoEntityInput) {
        pollAnswerJpaEntity.setApproved(pollAnswerDtoEntityInput.isApproved());
        return pollAnswerJpaEntity;
    }
    @Override
    public PollAnswer toEntity(PollAnswer pollAnswerDtoEntityInput, PollAnswer pollAnswerJpaEntityToBeModified) {
        return setJpaEntityVariables(pollAnswerJpaEntityToBeModified, pollAnswerDtoEntityInput);
    }
}
