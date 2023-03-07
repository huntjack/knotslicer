package com.knotslicer.server.adapters.jpa;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.EventDao;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional(rollbackOn={Exception.class})
public class EventDaoImpl implements EventDao {
    @PersistenceContext(unitName = "knotslicer_database")
    private EntityManager entityManager;

    @Override
    public Event create(Event event, Long userId) {
        UserImpl userImpl = getUserWithEventsFromJpa(userId);
        entityManager.detach(userImpl);
        userImpl.addEvent((EventImpl) event);
        userImpl = entityManager.merge(userImpl);
        entityManager.flush();
        return getEventFromUser(
                userImpl,
                event);
    }
    private UserImpl getUserWithEventsFromJpa(Long userId) {
        TypedQuery<UserImpl> query = entityManager.createQuery
                        ("SELECT user FROM User user " +
                                "LEFT JOIN FETCH user.events " +
                                "WHERE user.userId = :userId", UserImpl.class)
                .setParameter("userId", userId);
        return query.getSingleResult();
    }
    private Event getEventFromUser(UserImpl userImpl, Event event) {
        List<EventImpl> eventImpls = userImpl.getEvents();
        int eventIndex = eventImpls.indexOf(event);
        return eventImpls.get(eventIndex);
    }
    @Override
    public Optional<Event> get(Long eventId) {
        Event event = entityManager.find(EventImpl.class, eventId);
        return Optional.ofNullable(event);
    }
    @Override
    public User getPrimaryParent(Long eventId) {
        TypedQuery<UserImpl> query = entityManager.createQuery(
                "SELECT user FROM User user " +
                        "INNER JOIN user.events event " +
                        "WHERE event.eventId = :eventId", UserImpl.class)
                .setParameter("eventId", eventId);
        return query.getSingleResult();
    }
    @Override
    public Optional<User> getPrimaryParentWithChildren(Long userId) {
        User user = getUserWithEventsFromJpa(userId);
        return Optional.ofNullable(user);
    }
    @Override
    public Event getWithMembers(Long eventId) {
        return null;
    }
    @Override
    public Event update(Event eventInput, Long userId) {
        UserImpl userImpl = getUserWithEventsFromJpa(userId);
        entityManager.detach(userImpl);
        Event eventToBeModified = getEventFromUser(
                userImpl,
                eventInput);
        eventToBeModified.setEventName(
                eventInput.getEventName());
        eventToBeModified.setSubject(
                eventInput.getSubject());
        eventToBeModified.setEventDescription(
                eventInput.getEventDescription());
        userImpl = entityManager
                .merge(userImpl);
        entityManager.flush();
        return getEventFromUser(
                userImpl,
                eventToBeModified);
    }
    @Override
    public Event addMember(Long eventId, Long memberId) {
        Optional<MemberImpl> optionalMemberImpl = getMemberWithEvents(memberId);
        MemberImpl memberImpl = unpackOptional(optionalMemberImpl);
        Optional<EventImpl> optionalEventImpl = getEventWithMembers(eventId);
        EventImpl eventImpl = unpackOptional(optionalEventImpl);
        memberImpl.addEvent(eventImpl);
        entityManager.flush();
        return eventImpl;
    }
    private Optional<MemberImpl> getMemberWithEvents(Long memberId) {
        TypedQuery<MemberImpl> query = entityManager.createQuery
                        ("SELECT m FROM Member m " +
                                "LEFT JOIN FETCH m.events " +
                                "WHERE m.memberId = :memberId", MemberImpl.class)
                .setParameter("memberId", memberId);
        return Optional.ofNullable(
                query.getSingleResult());
    }
    private <T> T unpackOptional(Optional<T> optional) {
        return optional.orElseThrow(() -> new EntityNotFoundException("Entity not found."));
    }
    private Optional<EventImpl> getEventWithMembers(Long eventId) {
        TypedQuery<EventImpl> query = entityManager.createQuery
                        ("SELECT event FROM Event event " +
                                "LEFT JOIN FETCH event.members " +
                                "WHERE event.eventId = :eventId", EventImpl.class)
                .setParameter("eventId", eventId);
        return Optional.ofNullable(
                query.getSingleResult());
    }
    @Override
    public void removeMember(Long eventId, Long memberId) {
        Optional<MemberImpl> optionalMemberImpl = getMemberWithEvents(memberId);
        MemberImpl memberImpl = unpackOptional(optionalMemberImpl);
        Optional<EventImpl> optionalEventImpl = getEventWithMembers(eventId);
        EventImpl eventImpl = unpackOptional(optionalEventImpl);
        memberImpl.removeEvent(eventImpl);
        entityManager.flush();
    }
    @Override
    public void delete(Long eventId) {
        User user = getPrimaryParent(eventId);
        UserImpl userWithEvents =
                getUserWithEventsFromJpa(
                        user.getUserId());
        EventImpl eventImpl = entityManager
                .find(EventImpl.class, eventId);
        userWithEvents.removeEvent(eventImpl);
        entityManager.flush();
    }
}
