package com.knotslicer.server.ports.interactor.mappers;

import com.knotslicer.server.ports.interactor.EntityFactory;
import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.interactor.datatransferobjects.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
@Transactional
public class EntityDtoMapperImpl implements EntityDtoMapper {
    @Inject
    private EntityFactory entityFactory;
    @Inject
    private DtoFactory dtoFactory;
    @Override
    public UserDto toDto(User userInput) {
        UserDto userDto = dtoFactory.createUserDto();
        userDto.setUserId(userInput.getUserId());
        userDto.setEmail(userInput.getEmail());
        userDto.setUserName(userInput.getUserName());
        userDto.setUserDescription(userInput.getUserDescription());
        userDto.setTimeZone(userInput.getTimeZone());
        return userDto;
    }
    @Override
    public UserLightDto toLightDto(User userInput) {
        UserLightDto userLightDto = dtoFactory.createUserLightDto();
        userLightDto.setUserId(userInput.getUserId());
        userLightDto.setUserName(userInput.getUserName());
        userLightDto.setUserDescription(userInput.getUserDescription());
        userLightDto.setTimeZone(userInput.getTimeZone());
        return userLightDto;
    }
    @Override
    public User toEntity(UserDto userDtoInput) {
        User user = entityFactory.createUser();
        setEntityVariables(user, userDtoInput);
        return user;
    }
    private void setEntityVariables(User userToBeModified, UserDto userDtoInput) {
        userToBeModified.setEmail(userDtoInput.getEmail());
        userToBeModified.setUserName(userDtoInput.getUserName());
        userToBeModified.setUserDescription(userDtoInput.getUserDescription());
        userToBeModified.setTimeZone(userDtoInput.getTimeZone());
    }
    @Override
    public User toEntity(UserDto userDtoInput, User userToBeModified) {
        setEntityVariables(userToBeModified, userDtoInput);
        return userToBeModified;
    }
    @Override
    public User toEntity(UserLightDto userLightDtoInput, User userToBeModified) {
        userToBeModified.setUserName(userLightDtoInput.getUserName());
        userToBeModified.setUserDescription(userLightDtoInput.getUserDescription());
        userToBeModified.setTimeZone(userLightDtoInput.getTimeZone());
        return userToBeModified;
    }
    @Override
    public ProjectDto toDto(Project projectInput, Long userId) {
        ProjectDto projectDto = dtoFactory.createProjectDto();
        projectDto.setUserId(userId);
        projectDto.setProjectId(projectInput.getProjectId());
        projectDto.setProjectName(projectInput.getProjectName());
        projectDto.setProjectDescription(projectInput.getProjectDescription());
        return projectDto;
    }
    @Override
    public Project toEntity(ProjectDto projectDtoInput) {
        Project project = entityFactory.createProject();
        setEntityVariables(project, projectDtoInput);
        return project;
    }
    private void setEntityVariables(Project projectToBeModified, ProjectDto projectDtoInput) {
        projectToBeModified.setProjectName(projectDtoInput.getProjectName());
        projectToBeModified.setProjectDescription(projectDtoInput.getProjectDescription());
    }
    @Override
    public Project toEntity(ProjectDto projectDtoInput, Project projectToBeModified) {
        setEntityVariables(projectToBeModified, projectDtoInput);
        return projectToBeModified;
    }
    @Override
    public MemberDto toDto(Member memberInput, Long userId) {
        MemberDto memberDto = dtoFactory.createMemberDto();
        memberDto.setUserId(userId);
        memberDto.setMemberId(memberInput.getMemberId());
        memberDto.setName(memberInput.getName());
        memberDto.setRole(memberInput.getRole());
        memberDto.setRoleDescription(memberInput.getRoleDescription());
        return memberDto;
    }
    @Override
    public Member toEntity(MemberDto memberDtoInput) {
        Member member = entityFactory.createMember();
        setEntityVariables(member, memberDtoInput);
        return member;
    }
    private void setEntityVariables(Member memberToBeModified, MemberDto memberDtoInput) {
        memberToBeModified.setName(memberDtoInput.getName());
        memberToBeModified.setRole(memberDtoInput.getRole());
        memberToBeModified.setRoleDescription(memberDtoInput.getRoleDescription());
    }
    @Override
    public Member toEntity(MemberDto memberDtoInput, Member memberToBeModified) {
        setEntityVariables(memberToBeModified, memberDtoInput);
        return memberToBeModified;
    }
    @Override
    public EventDto toDto(Event eventInput, Long userId) {
        EventDto eventDto = dtoFactory.createEventDto();;
        eventDto.setUserId(userId);
        eventDto.setEventId(eventInput.getEventId());
        eventDto.setEventName(eventInput.getEventName());
        eventDto.setSubject(eventInput.getSubject());
        eventDto.setEventDescription(eventInput.getEventDescription());
        return eventDto;
    }
    @Override
    public Event toEntity(EventDto eventDtoInput) {
        Event event = entityFactory.createEvent();
        setEntityVariables(event, eventDtoInput);
        return event;
    }
    private void setEntityVariables(Event eventToBeModified, EventDto eventDtoInput) {
        eventToBeModified.setEventName(eventDtoInput.getEventName());
        eventToBeModified.setSubject(eventDtoInput.getSubject());
        eventToBeModified.setEventDescription(eventDtoInput.getEventDescription());
    }
    @Override
    public Event toEntity(EventDto eventDtoInput, Event eventToBeModified) {
        setEntityVariables(eventToBeModified, eventDtoInput);
        return eventToBeModified;
    }
    @Override
    public ScheduleDto toDto(Schedule scheduleInput, Long memberId) {
        ScheduleDto scheduleDto = dtoFactory.createScheduleDto();
        scheduleDto.setMemberId(memberId);
        scheduleDto.setScheduleId(scheduleInput.getScheduleId());
        scheduleDto.setStartTimeUtc(scheduleInput.getStartTimeUtc());
        scheduleDto.setEndTimeUtc(scheduleInput.getEndTimeUtc());
        return scheduleDto;
    }
    @Override
    public Schedule toEntity(ScheduleDto scheduleDtoInput) {
        Schedule schedule = entityFactory.createSchedule();
        setEntityVariables(schedule, scheduleDtoInput);
        return schedule;
    }
    private void setEntityVariables(Schedule scheduleToBeModified, ScheduleDto scheduleDtoInput) {
        scheduleToBeModified.setStartTimeUtc(scheduleDtoInput.getStartTimeUtc());
        scheduleToBeModified.setEndTimeUtc(scheduleDtoInput.getEndTimeUtc());
    }
    @Override
    public Schedule toEntity(ScheduleDto scheduleDtoInput, Schedule scheduleToBeModified) {
        setEntityVariables(scheduleToBeModified, scheduleDtoInput);
        return scheduleToBeModified;
    }
    @Override
    public PollDto toDto(Poll pollInput, Long eventId) {
        PollDto pollDto = dtoFactory.createPollDto();
        pollDto.setEventId(eventId);
        pollDto.setPollId(pollInput.getPollId());
        pollDto.setStartTimeUtc(pollInput.getStartTimeUtc());
        pollDto.setEndTimeUtc(pollInput.getEndTimeUtc());
        return pollDto;
    }
    @Override
    public Poll toEntity(PollDto pollDtoInput) {
        Poll poll = entityFactory.createPoll();
        setEntityVariables(poll, pollDtoInput);
        return poll;
    }
    private void setEntityVariables(Poll pollToBeModified, PollDto pollDtoInput) {
        pollToBeModified.setStartTimeUtc(pollDtoInput.getStartTimeUtc());
        pollToBeModified.setEndTimeUtc(pollDtoInput.getEndTimeUtc());
    }
    @Override
    public Poll toEntity(PollDto pollDtoInput, Poll pollToBeModified) {
        setEntityVariables(pollToBeModified, pollDtoInput);
        return pollToBeModified;
    }
    @Override
    public PollAnswerDto toDto(PollAnswer pollAnswerInput, Long pollId) {
        PollAnswerDto pollAnswerDto = dtoFactory.createPollAnswerDto();
        pollAnswerDto.setPollId(pollId);
        pollAnswerDto.setPollAnswerId(pollAnswerInput.getPollAnswerId());
        pollAnswerDto.setApproved(pollAnswerInput.isApproved());
        return pollAnswerDto;
    }
    @Override
    public PollAnswer toEntity(PollAnswerDto pollAnswerDtoInput) {
        PollAnswer pollAnswer = entityFactory.createPollAnswer();
        setEntityVariables(pollAnswer, pollAnswerDtoInput);
        return pollAnswer;
    }
    private void setEntityVariables(PollAnswer pollAnswerToBeModified, PollAnswerDto pollAnswerDtoInput) {
        pollAnswerToBeModified.setApproved(pollAnswerDtoInput.isApproved());
    }
    @Override
    public PollAnswer toEntity(PollAnswerDto pollAnswerDtoInput, PollAnswer pollAnswerToBeModified) {
        setEntityVariables(pollAnswerToBeModified, pollAnswerDtoInput);
        return pollAnswerToBeModified;
    }
}
