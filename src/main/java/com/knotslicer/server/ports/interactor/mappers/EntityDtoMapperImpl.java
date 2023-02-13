package com.knotslicer.server.ports.interactor.mappers;

import com.knotslicer.server.ports.interactor.EntityCreator;
import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.interactor.datatransferobjects.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class EntityDtoMapperImpl implements EntityDtoMapper {
    @Inject
    private EntityCreator entityCreator;
    @Inject
    private DtoCreator dtoCreator;
    @Override
    public UserDto toDto(User userInput) {
        UserDto userDto = dtoCreator.createUserDto();
        userDto.setUserId(
                userInput.getUserId());
        userDto.setEmail(
                userInput.getEmail());
        userDto.setUserName(
                userInput.getUserName());
        userDto.setUserDescription(
                userInput.getUserDescription());
        userDto.setTimeZone(
                userInput.getTimeZone());
        return userDto;
    }
    @Override
    public UserLightDto toLightDto(User userInput) {
        UserLightDto userLightDto = dtoCreator.createUserLightDto();
        userLightDto.setUserId(
                userInput.getUserId());
        userLightDto.setUserName(
                userInput.getUserName());
        userLightDto.setUserDescription(
                userInput.getUserDescription());
        userLightDto.setTimeZone(
                userInput.getTimeZone());
        return userLightDto;
    }
    @Override
    public User toEntity(UserDto userDtoInput) {
        User user = entityCreator.createUser();
        setEntityVariables(user, userDtoInput);
        return user;
    }
    private void setEntityVariables(User userToBeModified, UserDto userDtoInput) {
        userToBeModified.setEmail(
                userDtoInput.getEmail());
        userToBeModified.setUserName(
                userDtoInput.getUserName());
        userToBeModified.setUserDescription(
                userDtoInput.getUserDescription());
        userToBeModified.setTimeZone(
                userDtoInput.getTimeZone());
    }
    @Override
    public User toEntity(UserDto userDtoInput, User userToBeModified) {
        setEntityVariables(userToBeModified, userDtoInput);
        return userToBeModified;
    }
    @Override
    public User toEntity(UserLightDto userLightDtoInput, User userToBeModified) {
        userToBeModified.setUserName(
                userLightDtoInput.getUserName());
        userToBeModified.setUserDescription(
                userLightDtoInput.getUserDescription());
        userToBeModified.setTimeZone(
                userLightDtoInput.getTimeZone());
        return userToBeModified;
    }
    @Override
    public ProjectDto toDto(Project projectInput) {
        ProjectDto projectDto = dtoCreator.createProjectDto();
        setDtoVariables(projectDto, projectInput);
        return projectDto;
    }
    private void setDtoVariables(ProjectDto projectDtoToBeModified, Project projectInput) {
        projectDtoToBeModified.setProjectId(
                projectInput.getProjectId());
        projectDtoToBeModified.setProjectName(
                projectInput.getProjectName());
        projectDtoToBeModified.setProjectDescription(
                projectInput.getProjectDescription());
    }
    @Override
    public ProjectDto toDto(Project projectInput, Long userId) {
        ProjectDto projectDto = dtoCreator.createProjectDto();
        projectDto.setUserId(userId);
        setDtoVariables(projectDto, projectInput);
        return projectDto;
    }
    @Override
    public ProjectDto addMemberDtosToProjectDto(ProjectDto projectDto, Project projectInput) {
        ProjectImpl projectImpl = (ProjectImpl) projectInput;
        List<MemberImpl> membersImpls = projectImpl.getMembers();
        List<MemberLightDto> memberLightDtos = new LinkedList<>();
        Long projectId = projectImpl.getProjectId();
        for(MemberImpl memberImpl: membersImpls) {
            Long userId = memberImpl
                    .getUser()
                    .getUserId();
            MemberLightDto memberLightDto = toDto(
                    memberImpl,
                    userId,
                    projectId);
            memberLightDtos.add(memberLightDto);
        }
        projectDto.setMembers(memberLightDtos);
        return projectDto;
    }
    @Override
    public Project toEntity(ProjectDto projectDtoInput) {
        Project project = entityCreator.createProject();
        setEntityVariables(project, projectDtoInput);
        return project;
    }
    private void setEntityVariables(Project projectToBeModified, ProjectDto projectDtoInput) {
        projectToBeModified.setProjectName(
                projectDtoInput.getProjectName());
        projectToBeModified.setProjectDescription(
                projectDtoInput.getProjectDescription());
    }
    @Override
    public Project toEntity(ProjectDto projectDtoInput, Project projectToBeModified) {
        setEntityVariables(projectToBeModified, projectDtoInput);
        return projectToBeModified;
    }
    @Override
    public MemberLightDto toDto(Member memberInput) {
        MemberLightDto memberDto = dtoCreator.createMemberDto();
        setDtoVariables(memberDto, memberInput);
        return memberDto;
    }
    private void setDtoVariables(MemberLightDto memberDtoToBeModified, Member memberInput) {
        memberDtoToBeModified.setMemberId(
                memberInput.getMemberId());
        memberDtoToBeModified.setName(
                memberInput.getName());
        memberDtoToBeModified.setRole(
                memberInput.getRole());
        memberDtoToBeModified.setRoleDescription(
                memberInput.getRoleDescription());
    }
    @Override
    public MemberLightDto toDto(Member memberInput, Long userId, Long projectId) {
        MemberLightDto memberDto = dtoCreator.createMemberLightDto();
        memberDto.setUserId(userId);
        memberDto.setProjectId(projectId);
        setDtoVariables(memberDto, memberInput);
        return memberDto;
    }
    @Override
    public MemberDto toDto(Member memberInput, Long userId, Long projectId, Long projectOwnerId) {
        MemberDto memberDto = dtoCreator.createMemberDto();
        memberDto.setUserId(userId);
        memberDto.setProjectId(projectId);
        memberDto.setProjectOwnerId(projectOwnerId);
        setDtoVariables(memberDto, memberInput);
        return memberDto;
    }
    @Override
    public Member toEntity(MemberLightDto memberDtoInput) {
        Member member = entityCreator.createMember();
        setEntityVariables(member, memberDtoInput);
        return member;
    }
    private void setEntityVariables(Member memberToBeModified, MemberLightDto memberDtoInput) {
        memberToBeModified.setName(
                memberDtoInput.getName());
        memberToBeModified.setRole(
                memberDtoInput.getRole());
        memberToBeModified.setRoleDescription(
                memberDtoInput.getRoleDescription());
    }
    @Override
    public Member toEntity(MemberLightDto memberDtoInput, Member memberToBeModified) {
        setEntityVariables(memberToBeModified, memberDtoInput);
        return memberToBeModified;
    }
    @Override
    public EventDto toDto(Event eventInput, Long userId) {
        EventDto eventDto = dtoCreator.createEventDto();;
        eventDto.setUserId(userId);
        eventDto.setEventId(
                eventInput.getEventId());
        eventDto.setEventName(
                eventInput.getEventName());
        eventDto.setSubject(
                eventInput.getSubject());
        eventDto.setEventDescription(
                eventInput.getEventDescription());
        return eventDto;
    }
    @Override
    public Event toEntity(EventDto eventDtoInput) {
        Event event = entityCreator.createEvent();
        setEntityVariables(event, eventDtoInput);
        return event;
    }
    private void setEntityVariables(Event eventToBeModified, EventDto eventDtoInput) {
        eventToBeModified.setEventName(
                eventDtoInput.getEventName());
        eventToBeModified.setSubject(
                eventDtoInput.getSubject());
        eventToBeModified.setEventDescription(
                eventDtoInput.getEventDescription());
    }
    @Override
    public Event toEntity(EventDto eventDtoInput, Event eventToBeModified) {
        setEntityVariables(eventToBeModified, eventDtoInput);
        return eventToBeModified;
    }
    @Override
    public ScheduleDto toDto(Schedule scheduleInput, Long memberId) {
        ScheduleDto scheduleDto = dtoCreator.createScheduleDto();
        scheduleDto.setMemberId(memberId);
        scheduleDto.setScheduleId(
                scheduleInput.getScheduleId());
        scheduleDto.setStartTimeUtc(
                scheduleInput.getStartTimeUtc());
        scheduleDto.setEndTimeUtc(
                scheduleInput.getEndTimeUtc());
        return scheduleDto;
    }
    @Override
    public Schedule toEntity(ScheduleDto scheduleDtoInput) {
        Schedule schedule = entityCreator.createSchedule();
        setEntityVariables(schedule, scheduleDtoInput);
        return schedule;
    }
    private void setEntityVariables(Schedule scheduleToBeModified, ScheduleDto scheduleDtoInput) {
        scheduleToBeModified.setStartTimeUtc(
                scheduleDtoInput.getStartTimeUtc());
        scheduleToBeModified.setEndTimeUtc(
                scheduleDtoInput.getEndTimeUtc());
    }
    @Override
    public Schedule toEntity(ScheduleDto scheduleDtoInput, Schedule scheduleToBeModified) {
        setEntityVariables(scheduleToBeModified, scheduleDtoInput);
        return scheduleToBeModified;
    }
    @Override
    public PollDto toDto(Poll pollInput, Long eventId) {
        PollDto pollDto = dtoCreator.createPollDto();
        pollDto.setEventId(eventId);
        pollDto.setPollId(
                pollInput.getPollId());
        pollDto.setStartTimeUtc(
                pollInput.getStartTimeUtc());
        pollDto.setEndTimeUtc(
                pollInput.getEndTimeUtc());
        return pollDto;
    }
    @Override
    public Poll toEntity(PollDto pollDtoInput) {
        Poll poll = entityCreator.createPoll();
        setEntityVariables(poll, pollDtoInput);
        return poll;
    }
    private void setEntityVariables(Poll pollToBeModified, PollDto pollDtoInput) {
        pollToBeModified.setStartTimeUtc(
                pollDtoInput.getStartTimeUtc());
        pollToBeModified.setEndTimeUtc(
                pollDtoInput.getEndTimeUtc());
    }
    @Override
    public Poll toEntity(PollDto pollDtoInput, Poll pollToBeModified) {
        setEntityVariables(pollToBeModified, pollDtoInput);
        return pollToBeModified;
    }
    @Override
    public PollAnswerDto toDto(PollAnswer pollAnswerInput, Long pollId) {
        PollAnswerDto pollAnswerDto = dtoCreator.createPollAnswerDto();
        pollAnswerDto.setPollId(pollId);
        pollAnswerDto.setPollAnswerId(
                pollAnswerInput.getPollAnswerId());
        pollAnswerDto.setApproved(
                pollAnswerInput.isApproved());
        return pollAnswerDto;
    }
    @Override
    public PollAnswer toEntity(PollAnswerDto pollAnswerDtoInput) {
        PollAnswer pollAnswer = entityCreator.createPollAnswer();
        setEntityVariables(pollAnswer, pollAnswerDtoInput);
        return pollAnswer;
    }
    private void setEntityVariables(PollAnswer pollAnswerToBeModified, PollAnswerDto pollAnswerDtoInput) {
        pollAnswerToBeModified.setApproved(
                pollAnswerDtoInput.isApproved());
    }
    @Override
    public PollAnswer toEntity(PollAnswerDto pollAnswerDtoInput, PollAnswer pollAnswerToBeModified) {
        setEntityVariables(pollAnswerToBeModified, pollAnswerDtoInput);
        return pollAnswerToBeModified;
    }
    @Inject
    public EntityDtoMapperImpl(EntityCreator entityCreator, DtoCreator dtoCreator) {
        this.entityCreator = entityCreator;
        this.dtoCreator = dtoCreator;
    }
}
