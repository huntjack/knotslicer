package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.entitygateway.EventDao;
import com.knotslicer.server.ports.interactor.EntityCreator;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
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
        Set<Long> state = new HashSet<>();
        Set<Poll> solutions = new HashSet<>();
        Map<Set<Long>, Poll> memo = new HashMap<>();
        search(state, solutions, schedules, members, memo);
        logger.debug("findAvailableEventTimes() -> root search() call has been completed");
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
    private void search(Set<Long> state,
                        Set<Poll> solutions,
                        Map<Long, Schedule> schedules,
                        Set<Member> members,
                        Map<Set<Long>, Poll> memo) {
        logger.debug("search() is running -> state = " + state);
      if(hasValidState(state, memo, members, schedules)) {
          Poll pollCopy = entityCreator.copyPoll(
                  memo.get(state));
          logger.debug("search() -> state(" + state + ") is valid = adding poll(startTime: " +
                  pollCopy.getStartTimeUtc().toString() +" endTime: " + pollCopy.getEndTimeUtc().toString() + ") to solutions");
          solutions.add(pollCopy);
      }
      for(Long candidate: getCandidates(state, schedules, memo)) {
          state.add(candidate);
          logger.debug("search() -> state with new candidate = " + state);
          search(state, solutions, schedules, members, memo);
          state.remove(candidate);
      }
    }
    private Boolean hasValidState(Set<Long> state,
                                  Map<Set<Long>, Poll> memo,
                                  Set<Member> members,
                                  Map<Long, Schedule> schedules) {
        logger.debug("hasValidState() is running -> state = " + state);
        if(memo.containsKey(state)) {
            Poll poll = memo.get(state);
            LocalDateTime pollStartTime = poll.getStartTimeUtc();
            LocalDateTime pollEndTime = poll.getEndTimeUtc();
            if(pollStartTime.isBefore(LocalDateTime.MAX) &&
                    pollEndTime.isBefore(LocalDateTime.MAX) &&
                    memberCheck(state, members, schedules)) {
                logger.debug("hasValidState() -> state is valid");
                return true;
            } else {
                logger.debug("hasValidState() -> state is invalid");
                return false;
            }
        }
        logger.debug("hasValidState() -> state is not in memo");
        return false;
    }
    private Boolean memberCheck(Set<Long> state, Set<Member> members, Map<Long, Schedule> schedules) {
        logger.debug("memberCheck() is running -> state = " + state);
        Set<Member> membersInNode = new HashSet<>();
        for(Long scheduleId :state) {
            Schedule schedule = schedules.get(scheduleId);
            membersInNode.add(schedule.getMember());
        }
        if(membersInNode.containsAll(members)) {
            logger.debug("memberCheck() -> node contains all event members");
            return true;
        }
        logger.debug("memberCheck() -> node does not contain all event members");
        return false;
    }
    private List<Long> getCandidates(Set<Long> state,
                                     Map<Long, Schedule> schedules,
                                     Map<Set<Long>, Poll> memo) {
        logger.debug("getCandidates() is running -> state = " + state);
        List<Long> candidates = new LinkedList<>();
        //If state is empty, get all candidates(potential schedules)
        if (state.isEmpty()) {
            for (Map.Entry<Long, Schedule> mapElement : schedules.entrySet()) {
                candidates.add(mapElement.getKey());
            }
            return candidates;
        }
        //Otherwise, get all candidates that are not in the state and haven't been tried before
        Set<Long> stateCopy = new HashSet<>(state);
        logger.debug("getCandidates() -> getting candidates that are not in state and haven't been tried before");
        for (Map.Entry<Long, Schedule> mapElement : schedules.entrySet()) {
            Long scheduleId = mapElement.getKey();
            stateCopy.add(scheduleId);
            if (!state.contains(scheduleId) && !memo.containsKey(stateCopy)) {
                logger.debug("getCandidates() -> adding schedule("+scheduleId+") to candidates");
                candidates.add(scheduleId);
            }
            stateCopy.remove(scheduleId);
        }
        logger.debug("Current candidates before pruning: " + candidates);
        //Prune candidates + save overlap results in memo
        Set<Long> candidatesToBeRemoved = new HashSet<>();
        for (Long candidate : candidates) {
            logger.debug("getCandidates() check if state is in memo -> state = " + state + " | candidate = " + candidate);
            Schedule candidateSchedule = schedules.get(candidate);
            LocalDateTime candidateStartTime = candidateSchedule.getStartTimeUtc();
            LocalDateTime candidateEndTime = candidateSchedule.getEndTimeUtc();
            if (memo.containsKey(state)) {
                    /*If state's poll is in memo, compare new candidateSchedule with poll for overlaps and save new valid/invalid poll in memo,
                    otherwise the node only has one schedule and must be compared manually*/
                logger.debug("getCandidates() state is in memo -> state = " + state);
                Poll poll = memo.get(state);
                LocalDateTime stateNodeOverlapStart = poll.getStartTimeUtc();
                LocalDateTime stateNodeOverlapEnd = poll.getEndTimeUtc();
                if (!hasOverlap(candidateSchedule, stateNodeOverlapStart, stateNodeOverlapEnd)) {
                    logger.debug("getCandidates() -> stateNode(" + state + ") and candidate(" + candidate + ") do not overlap");
                    candidatesToBeRemoved.add(candidate);
                    //save invalid poll in memo
                    Set<Long> invalidNode = new HashSet<>();
                    invalidNode.addAll(state);
                    invalidNode.add(candidate);
                    addNodeToMemo(
                            invalidNode,
                            memo,
                            LocalDateTime.MAX,
                            LocalDateTime.MAX);
                } else {
                    logger.debug("getCandidates() -> stateNode(" + state + ") and candidateSchedule(" + candidate + ") overlap");
                    //save valid poll in memo
                    Set<Long> validNode = new HashSet<>();
                    validNode.addAll(state);
                    validNode.add(candidate);
                    //Determine Overlap
                    LocalDateTime candidateNodeOverlapStart =
                            findOverlapStart(candidateStartTime, stateNodeOverlapStart);
                    LocalDateTime candidateNodeOverlapEnd =
                            findOverlapEnd(candidateEndTime, stateNodeOverlapEnd);
                    addNodeToMemo(
                            validNode,
                            memo,
                            candidateNodeOverlapStart,
                            candidateNodeOverlapEnd);
                }
            } else {
                logger.debug("getCandidates() -> state(" + state + ") is not found in memo -> state has contains only one schedule");
                for (Long scheduleIdInNode : state) {
                    logger.debug("getCandidates() comparing candidate(" + candidate + ") and scheduleInNode = " + scheduleIdInNode);
                    Schedule scheduleInNode = schedules.get(scheduleIdInNode);
                    LocalDateTime scheduleInNodeStartTime = scheduleInNode.getStartTimeUtc();
                    LocalDateTime scheduleInNodeEndTime = scheduleInNode.getEndTimeUtc();
                    if (!hasOverlap(candidateSchedule, scheduleInNodeStartTime, scheduleInNodeEndTime)) {
                        logger.debug("getCandidates() -> nodeSchedule(" + scheduleIdInNode + ") and candidateSchedule(" + candidate +
                                ") do not overlap -> pruning candidate + adding invalid node to memo");
                        candidatesToBeRemoved.add(candidate);
                        //Add invalidNode to memo
                        Set<Long> invalidNode = new HashSet<>();
                        invalidNode.add(candidate);
                        invalidNode.add(scheduleIdInNode);
                        addNodeToMemo(
                                invalidNode,
                                memo,
                                LocalDateTime.MAX,
                                LocalDateTime.MAX);
                    } else {
                        logger.debug("getCandidates() -> nodeSchedule(" + scheduleIdInNode +") and candidateSchedule(" + candidate +
                                ") overlap -> adding valid node to memo");
                        //Add validNode to memo
                        Set<Long> validNode = new HashSet<>();
                        validNode.add(scheduleIdInNode);
                        validNode.add(candidate);
                        //Determine Overlap
                        LocalDateTime overlapStart =
                                findOverlapStart(candidateStartTime, scheduleInNodeStartTime);
                        LocalDateTime overlapEnd =
                                findOverlapEnd(candidateEndTime, scheduleInNodeEndTime);
                        addNodeToMemo(
                                validNode,
                                memo,
                                overlapStart,
                                overlapEnd);
                    }
                }
            }
        }
        logger.debug("getCandidates() -> candidates.removeAll(candidatesToBeRemoved)");
        candidates.removeAll(candidatesToBeRemoved);
        logger.debug("getCandidates() -> candidates after pruning: " + candidates);
        return candidates;
    }
    private Boolean hasOverlap(Schedule candidateSchedule, LocalDateTime nodeStartTime, LocalDateTime nodeEndTime) {
        logger.debug("hasOverlap() -> is running");
        LocalDateTime candidateStartTime = candidateSchedule.getStartTimeUtc();
        LocalDateTime candidateEndTime = candidateSchedule.getEndTimeUtc();
        if(candidateStartTime.isBefore(
                nodeEndTime.minusMinutes(15L)) &&
                candidateEndTime.isAfter(
                        nodeStartTime.plusMinutes(15L))) {
            logger.debug("hasOverlap() -> candidate and node overlap");
            return true;
        } else {
            logger.debug("hasOverlap() -> candidate and node do not overlap");
            return false;
        }
    }
    private void addNodeToMemo(Set<Long> node,
                               Map<Set<Long>, Poll> memo,
                               LocalDateTime pollStartTime,
                               LocalDateTime pollEndTime) {
        logger.debug("addNodeToMemo() -> is running");
        if(!memo.containsKey(node)) {
            Poll poll = entityCreator.createPoll();
            poll.setStartTimeUtc(pollStartTime);
            poll.setEndTimeUtc(pollEndTime);
            memo.put(node, poll);
        }
    }
    private LocalDateTime findOverlapStart(LocalDateTime candidateStartTime, LocalDateTime nodeStartTime) {
        logger.debug("findOverlapStart() -> is running");
        //This could take an extra parameter to add a buffer time for the overlap
        return candidateStartTime.isAfter(
                nodeStartTime) ? candidateStartTime : nodeStartTime;
    }
    private LocalDateTime findOverlapEnd(LocalDateTime candidateEndTime, LocalDateTime nodeEndTime) {
        logger.debug("findOverlapEnd() -> is running");
        //This could take an extra parameter to add a buffer time for the overlap
        return candidateEndTime.isBefore(
                nodeEndTime) ? candidateEndTime : nodeEndTime;
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
                            EntityCreator entityCreator) {
        this.entityDtoMapper = entityDtoMapper;
        this.eventDao = eventDao;
        this.pollDao = pollDao;
        this.entityCreator = entityCreator;
    }
    protected EventServiceImpl() {}
}
