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
    private EntityCreator entityCreator;
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
        User userToBeModified = entityCreator.createUser();
        userToBeModified.setEmail(
                userDtoInput.getEmail());
        userToBeModified.setUserName(
                userDtoInput.getUserName());
        userToBeModified.setUserDescription(
                userDtoInput.getUserDescription());
        userToBeModified.setTimeZone(
                userDtoInput.getTimeZone());
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
    public ProjectDto toDto(Project projectInput, Long userId) {
        ProjectDto projectDto = dtoCreator.createProjectDto();
        projectDto.setUserId(userId);
        projectDto.setProjectId(
                projectInput.getProjectId());
        projectDto.setProjectName(
                projectInput.getProjectName());
        projectDto.setProjectDescription(
                projectInput.getProjectDescription());
        return projectDto;
    }
    @Override
    public ProjectDto addMemberDtosToProjectDto(ProjectDto projectDto, Project projectInput) {
        ProjectImpl projectImpl = (ProjectImpl) projectInput;
        List<MemberImpl> membersImpls = projectImpl.getMembers();
        List<MemberDto> memberDtos = new LinkedList<>();
        Long projectId = projectImpl.getProjectId();
        for(MemberImpl memberImpl: membersImpls) {
            Long userId = memberImpl
                    .getUser()
                    .getUserId();
            MemberDto memberDto = toDto(
                    memberImpl,
                    userId,
                    projectId);
            memberDtos.add(memberDto);
        }
        projectDto.setMembers(memberDtos);
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
    public MemberDto toDto(Member memberInput, Long userId, Long projectId) {
        MemberDto memberDto = dtoCreator.createMemberDto();
        memberDto.setUserId(userId);
        memberDto.setProjectId(projectId);
        memberDto.setMemberId(
                memberInput.getMemberId());
        memberDto.setName(
                memberInput.getName());
        memberDto.setRole(
                memberInput.getRole());
        memberDto.setRoleDescription(
                memberInput.getRoleDescription());
        return memberDto;
    }
    public MemberDto addScheduleDtosToMemberDto(MemberDto memberDto, Member memberInput) {
        MemberImpl memberImpl = (MemberImpl) memberInput;
        List<ScheduleImpl> scheduleImpls = memberImpl.getSchedules();
        List<ScheduleDto> scheduleDtos = new LinkedList<>();
        Long memberId = memberInput.getMemberId();
        for(ScheduleImpl scheduleImpl: scheduleImpls) {
            ScheduleDto scheduleDto =
                    toDto(scheduleImpl, memberId);
            scheduleDtos.add(scheduleDto);
        }
        memberDto.setSchedules(scheduleDtos);
        return memberDto;
    }
    @Override
    public Member toEntity(MemberDto memberDtoInput) {
        Member member = entityCreator.createMember();
        setEntityVariables(member, memberDtoInput);
        return member;
    }
    private void setEntityVariables(Member memberToBeModified, MemberDto memberDtoInput) {
        memberToBeModified.setName(
                memberDtoInput.getName());
        memberToBeModified.setRole(
                memberDtoInput.getRole());
        memberToBeModified.setRoleDescription(
                memberDtoInput.getRoleDescription());
    }
    @Override
    public Member toEntity(MemberDto memberDtoInput, Member memberToBeModified) {
        setEntityVariables(memberToBeModified, memberDtoInput);
        return memberToBeModified;
    }
    @Override
    public EventDto toDto(Event eventInput, Long userId) {
        EventDto eventDto = dtoCreator.createEventDto();
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
    public EventDto addPollDtosToEventDto(EventDto eventDto, Event eventInput) {
        EventImpl eventImpl = (EventImpl) eventInput;
        List<PollImpl> pollImpls = eventImpl.getPolls();
        List<PollDto> pollDtos = new LinkedList<>();
        Long eventId = eventImpl.getEventId();
        for(PollImpl pollImpl: pollImpls) {
            PollDto pollDto =
                    toDto(pollImpl, eventId);
            pollDtos.add(pollDto);
        }
        eventDto.setPolls(pollDtos);
        return eventDto;
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
    public PollDto addPollAnswerDtosToPollDto(PollDto pollDto, Poll pollInput) {
        PollImpl pollImpl = (PollImpl) pollInput;
        List<PollAnswerImpl> pollAnswerImpls =
                pollImpl.getPollAnswers();
        List<PollAnswerDto> pollAnswerDtos = new LinkedList<>();
        Long pollId = pollInput.getPollId();
        for(PollAnswerImpl pollAnswerImpl : pollAnswerImpls) {
            Long memberId = pollAnswerImpl
                    .getMember()
                    .getMemberId();
            PollAnswerDto pollAnswerDto =
                    toDto(pollAnswerImpl,
                            pollId,
                            memberId);
            pollAnswerDtos.add(pollAnswerDto);
        }
        pollDto.setPollAnswers(pollAnswerDtos);
        return pollDto;
    }
    @Override
    public PollAnswerDto toDto(PollAnswer pollAnswerInput, Long pollId, Long memberId) {
        PollAnswerDto pollAnswerDto = dtoCreator.createPollAnswerDto();
        pollAnswerDto.setPollId(pollId);
        pollAnswerDto.setMemberId(memberId);
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
    protected EntityDtoMapperImpl() {}
}
