package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.entitygateway.EventDao;
import com.knotslicer.server.ports.interactor.*;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.EventMemberDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.PollDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import java.util.*;

@ApplicationScoped
public class EventServiceImpl implements EventService {
    private static final Logger logger
            = LoggerFactory.getLogger(EventServiceImpl.class);
    private EntityDtoMapper entityDtoMapper;
    private EventDao eventDao;
    private ChildWithOneRequiredParentDao<Poll, Event> pollDao;
    private FindEventTimesCommandCreator findEventTimesCommandCreator;
    private EntityCreator entityCreator;

    @Override
    public EventDto create(EventDto eventDto) {
        Event event = entityDtoMapper.toEntity(eventDto);
        Long userId = eventDto.getUserId();
        event = eventDao.create(event, userId);
        return entityDtoMapper
                .toDto(event, userId);
    }
    @Override
    public EventDto get(Long eventId) {
        Event event = getEvent(eventId);
        Long userId = getUserId(eventId);
        return entityDtoMapper
                .toDto(event, userId);
    }
    private Event getEvent(Long eventId) {
        Optional<Event> optionalEvent = eventDao.get(eventId);
         return optionalEvent
                .orElseThrow(() -> new EntityNotFoundException());
    }
    private Long getUserId(Long eventId) {
        Optional<User> optionalUser = eventDao.getPrimaryParent(eventId);
        User user = optionalUser
                .orElseThrow(() -> new EntityNotFoundException());
        return user.getUserId();
    }
    @Override
    public EventDto getWithChildren(Long eventId) {
        Optional<Event> optionalEvent =
                pollDao.getPrimaryParentWithChildren(eventId);
        Event event = optionalEvent
                .orElseThrow(() -> new EntityNotFoundException());
        Long userId = getUserId(eventId);
        EventDto eventDto = entityDtoMapper
                .toDto(event, userId);
        return entityDtoMapper
                .addPollDtosToEventDto(eventDto, event);
    }
    @Override
    public EventDto getWithMembers(Long eventId) {
        Optional<Event> optionalEventWithMembers =
                eventDao.getEventWithMembers(eventId);
        Event eventWithMembers = optionalEventWithMembers
                .orElseThrow(() -> new EntityNotFoundException());
        Long userId = getUserId(eventId);
        EventDto eventDto = entityDtoMapper
                .toDto(eventWithMembers, userId);
        return entityDtoMapper
                .addMemberDtosToEventDto(
                        eventDto,
                        eventWithMembers);
    }

    @Override
    public EventDto update(EventDto eventDto) {
        Long eventId = eventDto.getEventId();
        Event eventToBeModified = getEvent(eventId);
        eventToBeModified = entityDtoMapper
                .toEntity(eventDto, eventToBeModified);
        Long userId = getUserId(eventId);
        Event updatedEvent = eventDao
                .update(eventToBeModified,
                        userId);
        return entityDtoMapper
                .toDto(updatedEvent, userId);
    }
    @Override
    public void addMember(EventMemberDto eventMemberDto) {
        Long eventId = eventMemberDto.getEventId();
        Long memberId = eventMemberDto.getMemberId();
        eventDao.addMember(eventId, memberId);
    }
    @Override
    public void removeMember(Long eventId, Long memberId) {
        eventDao.removeMember(eventId, memberId);
    }
    @Override
    public List<PollDto> findAvailableEventTimes(Long eventId) {
        logger.debug("findAvailableEventTimes() -> is running");
        Map<Long, Schedule> schedules = getSchedulesMap(eventId);
        Set<Member> members = getMembersSet(eventId);
        Set<Poll> solutions = new HashSet<>();
        InteractorCommand interactorCommand = findEventTimesCommandCreator.createFindEventTimesCommand(
                schedules,
                members,
                solutions,
                entityCreator);
        InteractorCommandInvoker interactorCommandInvoker =
                findEventTimesCommandCreator.createCommandInvoker(interactorCommand);
        interactorCommandInvoker.executeCommand();
        List<PollDto> pollDtos = new LinkedList<>();
        packPollSolutionsIntoPollDtos(solutions, pollDtos, eventId);
        return pollDtos;
    }
    private Map<Long, Schedule> getSchedulesMap(Long eventId) {
        logger.debug("getSchedulesMap() -> is running");
        Optional<Set<Schedule>> optionalSchedulesOfMembers =
                eventDao.getSchedulesOfAllMembersAttendingEvent(eventId);
        Set<Schedule> schedulesOfMembers = optionalSchedulesOfMembers
                .orElseThrow(() -> new EntityNotFoundException());
        Map<Long, Schedule> schedulesMap = new HashMap<>();
        for(Schedule schedule : schedulesOfMembers) {
            schedulesMap.put(schedule.getScheduleId(), schedule);
        }
        return schedulesMap;
    }
    private Set<Member> getMembersSet(Long eventId) {
        logger.debug("getMemberSet() -> is running");
        Optional<Event> optionalEvent = eventDao.getEventWithMembers(eventId);
        EventImpl eventImpl = (EventImpl) optionalEvent
                .orElseThrow(() -> new EntityNotFoundException());
        Set<MemberImpl> memberImpls = eventImpl.getMembers();
        Set<Member> members = new HashSet<>();
        members.addAll(memberImpls);
        return members;
    }
    private List<PollDto> packPollSolutionsIntoPollDtos(Set<Poll> solutions, List<PollDto> pollDtos, Long eventId) {
        for(Poll poll : solutions) {
            logger.debug("Potential Meeting Start Time: " + poll.getStartTimeUtc().toString());
            logger.debug("Potential Meeting End Time: " + poll.getEndTimeUtc().toString());
            PollDto pollDto = entityDtoMapper.toDto(poll, eventId);
            pollDtos.add(pollDto);
        }
        return pollDtos;
    }

    @Override
    public void delete(Long eventId) {
        eventDao.delete(eventId);
    }
    @Inject
    public EventServiceImpl(EntityDtoMapper entityDtoMapper,
                            EventDao eventDao,
                            @ProcessAs(ProcessType.POLL)
                            ChildWithOneRequiredParentDao<Poll, Event> pollDao,
                            FindEventTimesCommandCreator findEventTimesCommandCreator,
                            EntityCreator entityCreator) {
        this.entityDtoMapper = entityDtoMapper;
        this.eventDao = eventDao;
        this.pollDao = pollDao;
        this.findEventTimesCommandCreator = findEventTimesCommandCreator;
        this.entityCreator = entityCreator;
    }
    protected EventServiceImpl() {}
}
