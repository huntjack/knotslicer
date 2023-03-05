package com.knotslicer.server.adapters.jpa;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
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
        EventImpl eventImpl = getEventWithPollsFromJpa(eventId);
        entityManager.detach(eventImpl);
        eventImpl.addPoll((PollImpl) poll);
        eventImpl = entityManager.merge(eventImpl);
        entityManager.flush();
        return getPollFromEvent(
                eventImpl,
                poll);
    }
    private EventImpl getEventWithPollsFromJpa(Long eventId) {
        TypedQuery<EventImpl> query = entityManager.createQuery
                        ("SELECT event FROM Event event " +
                                "LEFT JOIN FETCH event.polls " +
                                "WHERE event.eventId = :eventId", EventImpl.class)
                .setParameter("eventId", eventId);
        return query.getSingleResult();
    }
    private Poll getPollFromEvent(EventImpl eventImpl, Poll poll) {
        List<PollImpl> pollImpls = eventImpl.getPolls();
        int pollIndex = pollImpls.indexOf(poll);
        return pollImpls.get(pollIndex);
    }
    @Override
    public Optional<Poll> get(Long pollId) {
        Poll poll = entityManager.find(PollImpl.class, pollId);
        return Optional.ofNullable(poll);
    }
    @Override
    public Event getPrimaryParent(Long pollId) {
        TypedQuery<EventImpl> query = entityManager.createQuery(
                "SELECT event FROM Event event " +
                        "INNER JOIN event.polls poll " +
                        "WHERE poll.pollId = :pollId", EventImpl.class)
                .setParameter("pollId", pollId);
        return query.getSingleResult();
    }
    @Override
    public Optional<Event> getPrimaryParentWithChildren(Long eventId) {
        Event event = getEventWithPollsFromJpa(eventId);
        return Optional.ofNullable(event);
    }
    @Override
    public Poll update(Poll pollInput, Long eventId) {
        EventImpl eventImpl = getEventWithPollsFromJpa(eventId);
        entityManager.detach(eventImpl);
        Poll pollToBeModified =
                getPollFromEvent(eventImpl, pollInput);
        pollToBeModified.setStartTimeUtc(
                pollInput.getStartTimeUtc());
        pollToBeModified.setEndTimeUtc(
                pollInput.getEndTimeUtc());
        eventImpl = entityManager.merge(eventImpl);
        entityManager.flush();
        return getPollFromEvent(
                eventImpl,
                pollToBeModified);
    }

    @Override
    public void delete(Long pollId) {
        Event event = getPrimaryParent(pollId);
        EventImpl eventImpl =
                getEventWithPollsFromJpa(
                        event.getEventId());
        PollImpl pollImpl = entityManager
                .find(PollImpl.class, pollId);
        eventImpl.removePoll(pollImpl);
        entityManager.flush();
    }
}
