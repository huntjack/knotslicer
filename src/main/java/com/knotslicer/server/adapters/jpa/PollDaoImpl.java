package com.knotslicer.server.adapters.jpa;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.PollDao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional(rollbackOn={Exception.class})
public class PollDaoImpl implements PollDao {
    @PersistenceContext(unitName = "knotslicer_database")
    private EntityManager entityManager;

    @Override
    public Poll create(Poll poll, Long eventId) {
        EventImpl eventImpl = getEventWithPollsFromJpa(eventId);
        entityManager.detach(eventImpl);
        eventImpl.addPoll((PollImpl) poll);
        eventImpl = entityManager.merge(eventImpl);
        entityManager.flush();
        poll = getPollFromEvent(eventImpl, poll);
        entityManager.refresh(poll);
        return poll;
    }
    private EventImpl getEventWithPollsFromJpa(Long eventId) {
        TypedQuery<EventImpl> query = entityManager.createQuery
                        ("SELECT event FROM Event event " +
                                "INNER JOIN FETCH event.polls " +
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
    public Long getPrimaryParentId(Long pollId) {
        TypedQuery<EventImpl> query = entityManager.createQuery
                        ("SELECT event FROM Event event " +
                                "INNER JOIN event.polls poll " +
                                "WHERE poll.pollId = :pollId", EventImpl.class)
                .setParameter("pollId", pollId);
        Event event = query.getSingleResult();
        return event.getEventId();
    }
    @Override
    public Optional<Event> getPrimaryParentWithChildren(Long eventId) {
        Event event = getEventWithPollsFromJpa(eventId);
        return Optional.ofNullable(event);
    }
    @Override
    public Poll update(Poll poll, Long eventId) {
        EventImpl eventImpl = getEventWithPollsFromJpa(eventId);
        entityManager.detach(eventImpl);
        Poll pollToBeModified =
                getPollFromEvent(eventImpl, poll);
        pollToBeModified.setStartTimeUtc(
                poll.getStartTimeUtc());
        pollToBeModified.setEndTimeUtc(
                poll.getEndTimeUtc());
        eventImpl = entityManager.merge(eventImpl);
        entityManager.flush();
        Poll updatedPoll = getPollFromEvent(eventImpl, pollToBeModified);
        return updatedPoll;
    }

    @Override
    public void delete(Long pollId, Long eventId) {
        EventImpl eventImpl = getEventWithPollsFromJpa(eventId);
        PollImpl pollImpl = entityManager.find(PollImpl.class, pollId);
        eventImpl.removePoll(pollImpl);
        entityManager.flush();
    }
}
