package com.knotslicer.server.ports.interactor;

import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Poll;
import com.knotslicer.server.domain.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.LocalDateTime;
import java.util.*;

public class FindEventTimesCommandImpl implements FindEventTimesCommand {
    private final Map<Long, Schedule> schedules;
    private final Set<Member> members;
    private final EntityCreator entityCreator;
    private final Map<Set<Long>, Poll> memo = new HashMap<>(250);
    private final Set<Poll> solutions = new HashSet<>();
    private static final Logger logger
            = LoggerFactory.getLogger(FindEventTimesCommandImpl.class);
    public FindEventTimesCommandImpl(Map<Long, Schedule> schedules,
                                     Set<Member> members,
                                     EntityCreator entityCreator) {
        this.schedules = schedules;
        this.members = members;
        this.entityCreator = entityCreator;
    }

    @Override
    public Set<Poll> execute() {
        Set<Long> state = new HashSet<>();
        search(state);
        logger.debug("FindEventTimesCommand.execute() -> root search() call has been completed");
        logger.debug("memo size:" + memo.size());
        return solutions;
    }
    private void search(Set<Long> state) {
        logger.debug("search() is running -> state = " + state);
        if(hasValidState(state)) {
            Poll pollCopy = entityCreator.copyPoll(
                    memo.get(state));
            logger.debug("search() -> state(" + state + ") is valid = adding poll(startTime: " +
                    pollCopy.getStartTimeUtc().toString() +" endTime: " + pollCopy.getEndTimeUtc().toString() + ") to solutions");
            solutions.add(pollCopy);
        }
        for(Long candidate: getCandidates(state)) {
            state.add(candidate);
            logger.debug("search() -> state with new candidate = " + state);
            search(state);
            state.remove(candidate);
        }
    }
    private Boolean hasValidState(Set<Long> state) {
        logger.debug("hasValidState() is running -> state = " + state);
        if(memo.containsKey(state)) {
            Poll poll = memo.get(state);
            LocalDateTime pollStartTime = poll.getStartTimeUtc();
            LocalDateTime pollEndTime = poll.getEndTimeUtc();
            if(pollStartTime.isBefore(LocalDateTime.MAX) &&
                    pollEndTime.isBefore(LocalDateTime.MAX) &&
                    memberCheck(state)) {
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
    private Boolean memberCheck(Set<Long> state) {
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
    private Set<Long> getCandidates(Set<Long> state) {
        logger.debug("getCandidates() is running -> state = " + state);
        if (state.isEmpty()) {
            return new HashSet<>(schedules.keySet());
        }
        Set<Long> candidates = addUntriedCandidates(state);
        logger.debug("Current candidates before pruning: " + candidates);
        Set<Long> candidatesToBeRemoved = pruneCandidates(state, candidates);
        candidates.removeAll(candidatesToBeRemoved);
        logger.debug("getCandidates() -> candidates after pruning: " + candidates);
        return candidates;
    }
    private Set<Long> addUntriedCandidates(Set<Long> state) {
        Set<Long> candidates = new HashSet<>();
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
        return candidates;
    }
    private Set<Long> pruneCandidates(Set<Long> state, Set<Long> candidates) {
        Set<Long> candidatesToBeRemoved = new HashSet<>();
        for (Long candidate : candidates) {
            logger.debug("pruneCandidates() check if state is in memo -> state = " + state + " | candidate = " + candidate);
            Schedule candidateSchedule = schedules.get(candidate);
            LocalDateTime candidateStartTime = candidateSchedule.getStartTimeUtc();
            LocalDateTime candidateEndTime = candidateSchedule.getEndTimeUtc();
            String endOfNoOverlapMessage = ") do not overlap -> pruning candidates and adding invalid node to memo";
            String endOfOverlapMessage = ") overlap -> adding valid node to memo";
            if (memo.containsKey(state)) {
                logger.debug("pruneCandidates() state is in memo -> state = " + state);
                Poll poll = memo.get(state);
                LocalDateTime stateNodeOverlapStart = poll.getStartTimeUtc();
                LocalDateTime stateNodeOverlapEnd = poll.getEndTimeUtc();
                if (!hasOverlap(candidateSchedule, stateNodeOverlapStart, stateNodeOverlapEnd)) {
                    logger.debug("pruneCandidates() -> stateNode(" + state + ") and candidate(" + candidate + endOfNoOverlapMessage);
                    candidatesToBeRemoved.add(candidate);
                    Set<Long> invalidNode = new HashSet<>(state);
                    invalidNode.add(candidate);
                    addNodeToMemo(
                            invalidNode,
                            LocalDateTime.MAX,
                            LocalDateTime.MAX);
                } else {
                    logger.debug("pruneCandidates() -> stateNode(" + state + ") and candidateSchedule(" + candidate + endOfOverlapMessage);
                    Set<Long> validNode = new HashSet<>(state);
                    validNode.add(candidate);
                    LocalDateTime candidateNodeOverlapStart =
                            findOverlapStart(candidateStartTime, stateNodeOverlapStart);
                    LocalDateTime candidateNodeOverlapEnd =
                            findOverlapEnd(candidateEndTime, stateNodeOverlapEnd);
                    addNodeToMemo(
                            validNode,
                            candidateNodeOverlapStart,
                            candidateNodeOverlapEnd);
                }
            } else {
                logger.debug("pruneCandidates() -> state(" + state + ") is not found in memo -> state has contains only one schedule");
                for (Long scheduleIdInNode : state) {
                    logger.debug("pruneCandidates() comparing candidate(" + candidate + ") and scheduleInNode = " + scheduleIdInNode);
                    Schedule scheduleInNode = schedules.get(scheduleIdInNode);
                    LocalDateTime scheduleInNodeStartTime = scheduleInNode.getStartTimeUtc();
                    LocalDateTime scheduleInNodeEndTime = scheduleInNode.getEndTimeUtc();
                    if (!hasOverlap(candidateSchedule, scheduleInNodeStartTime, scheduleInNodeEndTime)) {
                        logger.debug("pruneCandidates() -> nodeSchedule(" + scheduleIdInNode + ") and candidateSchedule(" +
                                candidate + endOfNoOverlapMessage);
                        candidatesToBeRemoved.add(candidate);
                        Set<Long> invalidNode = new HashSet<>();
                        invalidNode.add(candidate);
                        invalidNode.add(scheduleIdInNode);
                        addNodeToMemo(
                                invalidNode,
                                LocalDateTime.MAX,
                                LocalDateTime.MAX);
                    } else {
                        logger.debug("pruneCandidates() -> nodeSchedule(" + scheduleIdInNode +") and candidateSchedule(" +
                                        candidate + endOfOverlapMessage);
                        Set<Long> validNode = new HashSet<>();
                        validNode.add(scheduleIdInNode);
                        validNode.add(candidate);
                        LocalDateTime overlapStart =
                                findOverlapStart(candidateStartTime, scheduleInNodeStartTime);
                        LocalDateTime overlapEnd =
                                findOverlapEnd(candidateEndTime, scheduleInNodeEndTime);
                        addNodeToMemo(
                                validNode,
                                overlapStart,
                                overlapEnd);
                    }
                }
            }
        }
        return candidatesToBeRemoved;
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
}
