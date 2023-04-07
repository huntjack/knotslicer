package com.knotslicer.server.ports.interactor.mappers;

import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.interactor.EntityCreator;
import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.*;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Optional;

@ApplicationScoped
public class EntityDtoMapperImpl implements EntityDtoMapper {
    private EntityCreator entityCreator;
    private DtoCreator dtoCreator;
    private ChildWithTwoParentsDao<Member, User, Project> memberDao;
    private ChildWithTwoParentsDao<PollAnswer, Poll, Member> pollAnswerDao;

    @Override
    public UserLightDto toDto(User userInput) {
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
    public UserLightDto addProjectDtosToUserLightDto(UserLightDto userLightDto, User userInput) {
        UserImpl userImpl = (UserImpl) userInput;
        List<ProjectImpl> projectImpls = userImpl.getProjects();
        List<ProjectDto> projectDtos = new LinkedList<>();
        Long userId = userInput.getUserId();
        for(ProjectImpl projectImpl: projectImpls) {
            ProjectDto projectDto = toDto(
                    projectImpl,
                    userId);
            projectDtos.add(projectDto);
        }
        userLightDto.setProjects(projectDtos);
        return userLightDto;
    }
    @Override
    public UserLightDto addMemberDtosToUserLightDto(UserLightDto userLightDto, User userInput) {
        UserImpl userImpl = (UserImpl) userInput;
        List<MemberImpl> memberImpls = userImpl.getMembers();
        List<MemberDto> memberDtos = new LinkedList<>();
        Long userId = userInput.getUserId();
        for(MemberImpl memberImpl: memberImpls) {
            Long memberId = memberImpl.getMemberId();
            Long projectId = getMembersSecondaryParentId(memberId);
            MemberDto memberDto = toDto(
                    memberImpl,
                    userId,
                    projectId);
            memberDtos.add(memberDto);
        }
        userLightDto.setMembers(memberDtos);
        return userLightDto;
    }
    private Long getMembersSecondaryParentId(Long memberId) {
        Optional<Project> optionalProject = memberDao.getSecondaryParent(memberId);
        Project project = optionalProject
                .orElseThrow(() -> new EntityNotFoundException());
        return project.getProjectId();
    }
    @Override
    public UserLightDto addEventDtosToUserLightDto(UserLightDto userLightDto, User userInput) {
        UserImpl userImpl = (UserImpl) userInput;
        List<EventImpl> eventImpls = userImpl.getEvents();
        List<EventDto> eventDtos = new LinkedList<>();
        Long userId = userInput.getUserId();
        for(EventImpl eventImpl: eventImpls) {
            EventDto eventDto = toDto(
                    eventImpl,
                    userId);
            eventDtos.add(eventDto);
        }
        userLightDto.setEvents(eventDtos);
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
            Long memberId = memberImpl.getMemberId();
            Long userId = getMembersPrimaryParentId(memberId);
            MemberDto memberDto = toDto(
                    memberImpl,
                    userId,
                    projectId);
            memberDtos.add(memberDto);
        }
        projectDto.setMembers(memberDtos);
        return projectDto;
    }
    private Long getMembersPrimaryParentId(Long memberId) {
        Optional<User> optionalUser = memberDao
                .getPrimaryParent(memberId);
        User user = optionalUser
                .orElseThrow(() -> new EntityNotFoundException());
        return user.getUserId();
    }
    @Override
    public Project toEntity(ProjectDto projectDtoInput) {
        Project project = entityCreator.createProject();
        setEntityFields(project, projectDtoInput);
        return project;
    }
    private void setEntityFields(Project projectToBeModified, ProjectDto projectDtoInput) {
        projectToBeModified.setProjectName(
                projectDtoInput.getProjectName());
        projectToBeModified.setProjectDescription(
                projectDtoInput.getProjectDescription());
    }
    @Override
    public Project toEntity(ProjectDto projectDtoInput, Project projectToBeModified) {
        setEntityFields(projectToBeModified, projectDtoInput);
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
    @Override
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
    public MemberDto addEventDtosToMemberDto(MemberDto memberDto, Member memberInput) {
        MemberImpl memberImpl = (MemberImpl) memberInput;
        Set<EventImpl> eventImpls = memberImpl.getEvents();
        List<EventDto> eventDtos = new LinkedList<>();
        for(EventImpl eventImpl: eventImpls) {
            EventDto eventDto = toDto(eventImpl);
            eventDtos.add(eventDto);
        }
        memberDto.setEvents(eventDtos);
        return memberDto;
    }
    @Override
    public Member toEntity(MemberDto memberDtoInput) {
        Member member = entityCreator.createMember();
        setEntityFields(member, memberDtoInput);
        return member;
    }
    private void setEntityFields(Member memberToBeModified, MemberDto memberDtoInput) {
        memberToBeModified.setName(
                memberDtoInput.getName());
        memberToBeModified.setRole(
                memberDtoInput.getRole());
        memberToBeModified.setRoleDescription(
                memberDtoInput.getRoleDescription());
    }
    @Override
    public Member toEntity(MemberDto memberDtoInput, Member memberToBeModified) {
        setEntityFields(memberToBeModified, memberDtoInput);
        return memberToBeModified;
    }
    @Override
    public EventDto toDto(Event eventInput) {
        EventDto eventDto = dtoCreator.createEventDto();
        setDtoFields(eventDto, eventInput);
        return eventDto;
    }
    private void setDtoFields(EventDto eventDto, Event eventInput) {
        eventDto.setEventId(
                eventInput.getEventId());
        eventDto.setEventName(
                eventInput.getEventName());
        eventDto.setSubject(
                eventInput.getSubject());
        eventDto.setEventDescription(
                eventInput.getEventDescription());
    }
    @Override
    public EventDto toDto(Event eventInput, Long userId) {
        EventDto eventDto = dtoCreator.createEventDto();
        eventDto.setUserId(userId);
        setDtoFields(eventDto, eventInput);
        return eventDto;
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
    public EventDto addMemberDtosToEventDto(EventDto eventDto, Event eventInput) {
        EventImpl eventImpl = (EventImpl) eventInput;
        Set<MemberImpl> memberImpls = eventImpl.getMembers();
        List<MemberDto> memberDtos = new LinkedList<>();
        for(MemberImpl memberImpl: memberImpls) {
            Long memberId = memberImpl.getMemberId();
            Long userId = getMembersPrimaryParentId(memberId);
            Long projectId = getMembersSecondaryParentId(memberId);
            MemberDto memberDto = toDto(
                    memberImpl,
                    userId,
                    projectId);
            memberDtos.add(memberDto);
        }
        eventDto.setMembers(memberDtos);
        return eventDto;
    }
    @Override
    public Event toEntity(EventDto eventDtoInput) {
        Event event = entityCreator.createEvent();
        setEntityFields(event, eventDtoInput);
        return event;
    }
    private void setEntityFields(Event eventToBeModified, EventDto eventDtoInput) {
        eventToBeModified.setEventName(
                eventDtoInput.getEventName());
        eventToBeModified.setSubject(
                eventDtoInput.getSubject());
        eventToBeModified.setEventDescription(
                eventDtoInput.getEventDescription());
    }
    @Override
    public Event toEntity(EventDto eventDtoInput, Event eventToBeModified) {
        setEntityFields(eventToBeModified, eventDtoInput);
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
        setEntityFields(schedule, scheduleDtoInput);
        return schedule;
    }
    private void setEntityFields(Schedule scheduleToBeModified, ScheduleDto scheduleDtoInput) {
        scheduleToBeModified.setStartTimeUtc(
                scheduleDtoInput.getStartTimeUtc());
        scheduleToBeModified.setEndTimeUtc(
                scheduleDtoInput.getEndTimeUtc());
    }
    @Override
    public Schedule toEntity(ScheduleDto scheduleDtoInput, Schedule scheduleToBeModified) {
        setEntityFields(scheduleToBeModified, scheduleDtoInput);
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
    public PollDto addPollAnswerDtosToPollDto(PollDto pollDto, Poll pollInput) {
        PollImpl pollImpl = (PollImpl) pollInput;
        List<PollAnswerImpl> pollAnswerImpls =
                pollImpl.getPollAnswers();
        List<PollAnswerDto> pollAnswerDtos = new LinkedList<>();
        Long pollId = pollInput.getPollId();
        for(PollAnswerImpl pollAnswerImpl : pollAnswerImpls) {
            Long pollAnswerId = pollAnswerImpl.getPollAnswerId();
            Optional<Member> optionalMember = pollAnswerDao
                    .getSecondaryParent(pollAnswerId);
            Member member = optionalMember
                    .orElseThrow(() -> new EntityNotFoundException());
            Long memberId =  member.getMemberId();;
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
    public Poll toEntity(PollDto pollDtoInput) {
        Poll poll = entityCreator.createPoll();
        setEntityFields(poll, pollDtoInput);
        return poll;
    }
    private void setEntityFields(Poll pollToBeModified, PollDto pollDtoInput) {
        pollToBeModified.setStartTimeUtc(
                pollDtoInput.getStartTimeUtc());
        pollToBeModified.setEndTimeUtc(
                pollDtoInput.getEndTimeUtc());
    }
    @Override
    public Poll toEntity(PollDto pollDtoInput, Poll pollToBeModified) {
        setEntityFields(pollToBeModified, pollDtoInput);
        return pollToBeModified;
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
        setEntityFields(pollAnswer, pollAnswerDtoInput);
        return pollAnswer;
    }
    private void setEntityFields(PollAnswer pollAnswerToBeModified, PollAnswerDto pollAnswerDtoInput) {
        pollAnswerToBeModified.setApproved(
                pollAnswerDtoInput.isApproved());
    }
    @Override
    public PollAnswer toEntity(PollAnswerDto pollAnswerDtoInput, PollAnswer pollAnswerToBeModified) {
        setEntityFields(pollAnswerToBeModified, pollAnswerDtoInput);
        return pollAnswerToBeModified;
    }
    @Inject
    public EntityDtoMapperImpl(EntityCreator entityCreator,
                               DtoCreator dtoCreator,
                               @ProcessAs(ProcessType.MEMBER)
                               ChildWithTwoParentsDao<Member, User, Project> memberDao,
                               @ProcessAs(ProcessType.POLLANSWER)
                               ChildWithTwoParentsDao<PollAnswer, Poll, Member> pollAnswerDao) {
        this.entityCreator = entityCreator;
        this.dtoCreator = dtoCreator;
        this.memberDao = memberDao;
        this.pollAnswerDao = pollAnswerDao;
    }
    protected EntityDtoMapperImpl() {}
}
