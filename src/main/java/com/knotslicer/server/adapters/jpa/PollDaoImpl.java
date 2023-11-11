package com.knotslicer.server.adapters.jpa;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@ProcessAs(ProcessType.POLL)
@ApplicationScoped
@Transactional(rollbackOn={Exception.class})
public class PollDaoImpl implements ChildWithOneRequiredParentDao<Poll, Event> {
    @PersistenceContext(unitName = "knotslicer_database")
    private EntityManager entityManager;

    @Override
    public Poll create(Poll poll, Long eventId) {
        EventImpl eventWithPolls = getEventImplWithPolls(eventId);
        entityManager.detach(eventWithPolls);
        eventWithPolls.addPoll((PollImpl) poll);
        eventWithPolls = entityManager.merge(eventWithPolls);
        entityManager.flush();
        Optional<Poll> optionalPollResponse =
                getPollFromEvent(
                        eventWithPolls,
                        poll);
        return optionalPollResponse
                .orElseThrow(() -> new EntityNotFoundException());
    }
    private EventImpl getEventImplWithPolls(Long eventId) {
        Optional<Event> optionalEventWithPolls = getPrimaryParentWithChildren(eventId);
        return (EventImpl) optionalEventWithPolls
                .orElseThrow(() -> new EntityNotFoundException());
    }
    @Override
    public Optional<Event> getPrimaryParentWithChildren(Long eventId) {
        TypedQuery<EventImpl> query = entityManager
                .createNamedQuery("getEventWithPolls", EventImpl.class)
                .setParameter("eventId", eventId);
        Event event = query.getSingleResult();
        return Optional.ofNullable(event);
    }
    private Optional<Poll> getPollFromEvent(EventImpl eventImpl, Poll poll) {
        List<PollImpl> pollImpls = eventImpl.getPolls();
        int pollIndex = pollImpls.indexOf(poll);
        poll = pollImpls.get(pollIndex);
        return Optional.ofNullable(poll);
    }
    @Override
    public Optional<Poll> get(Long pollId) {
        Poll poll = entityManager.find(PollImpl.class, pollId);
        return Optional.ofNullable(poll);
    }
    @Override
    public Optional<Event> getPrimaryParent(Long pollId) {
        TypedQuery<EventImpl> query = entityManager.createQuery(
                "SELECT event FROM Event event " +
                        "INNER JOIN event.polls poll " +
                        "WHERE poll.pollId = :pollId", EventImpl.class)
                .setParameter("pollId", pollId);
        Event event = query.getSingleResult();
        return Optional.ofNullable(event);
    }
    @Override
    public Poll update(Poll pollInput, Long eventId) {
        EventImpl eventWithPolls = getEventImplWithPolls(eventId);
        Optional<Poll> optionalPollToBeModified =
                getPollFromEvent(eventWithPolls, pollInput);
        Poll pollToBeModified = optionalPollToBeModified
                .orElseThrow(() -> new EntityNotFoundException());
        entityManager.detach(eventWithPolls);
        pollToBeModified.setStartTimeUtc(
                pollInput.getStartTimeUtc());
        pollToBeModified.setEndTimeUtc(
                pollInput.getEndTimeUtc());
        eventWithPolls = entityManager.merge(eventWithPolls);
        entityManager.flush();
        Optional<Poll> optionalPollResponse =
                getPollFromEvent(
                        eventWithPolls,
                        pollToBeModified);
        return optionalPollResponse
                .orElseThrow(() -> new EntityNotFoundException());
    }

    @Override
    public void delete(Long pollId) {
        Optional<Event> optionalEvent = getPrimaryParent(pollId);
        Event event = optionalEvent
                .orElseThrow(() -> new EntityNotFoundException());
        Long eventId = event.getEventId();
        EventImpl eventWithPolls = getEventImplWithPolls(eventId);
        Optional<Poll> optionalPoll = get(pollId);
        PollImpl pollImpl = (PollImpl) optionalPoll
                .orElseThrow(() -> new EntityNotFoundException());
        eventWithPolls.removePoll(pollImpl);
        entityManager.flush();
    }
}
