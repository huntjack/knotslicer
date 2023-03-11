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
import java.util.Set;

@ApplicationScoped
@Transactional(rollbackOn={Exception.class})
public class EventDaoImpl implements EventDao {
    @PersistenceContext(unitName = "knotslicer_database")
    private EntityManager entityManager;

    @Override
    public Event create(Event event, Long userId) {
        UserImpl userWithEvents = getUserImplWithEvents(userId);
        entityManager.detach(userWithEvents);
        userWithEvents.addEvent((EventImpl) event);
        userWithEvents = entityManager.merge(userWithEvents);
        entityManager.flush();
        Optional<Event> optionalEvent = getEventFromUser(
                userWithEvents,
                event);
        return optionalEvent
                .orElseThrow(() -> new EntityNotFoundException());
    }
    private UserImpl getUserImplWithEvents(Long userId) {
        Optional<User> optionalUserWithEvents = getPrimaryParentWithChildren(userId);
        return (UserImpl) optionalUserWithEvents
                .orElseThrow(() -> new EntityNotFoundException());
    }
    @Override
    public Optional<User> getPrimaryParentWithChildren(Long userId) {
        TypedQuery<UserImpl> query = entityManager.createQuery
                        ("SELECT user FROM User user " +
                                "LEFT JOIN FETCH user.events " +
                                "WHERE user.userId = :userId", UserImpl.class)
                .setParameter("userId", userId);
        User user = query.getSingleResult();
        return Optional.ofNullable(user);
    }
    private Optional<Event> getEventFromUser(UserImpl userImpl, Event event) {
        List<EventImpl> eventImpls = userImpl.getEvents();
        int eventIndex = eventImpls.indexOf(event);
        event = eventImpls.get(eventIndex);
        return Optional.ofNullable(event);
    }
    @Override
    public Optional<Event> get(Long eventId) {
        Event event = entityManager.find(EventImpl.class, eventId);
        return Optional.ofNullable(event);
    }
    @Override
    public Optional<User> getPrimaryParent(Long eventId) {
        TypedQuery<UserImpl> query = entityManager.createQuery(
                "SELECT user FROM User user " +
                        "INNER JOIN user.events event " +
                        "WHERE event.eventId = :eventId", UserImpl.class)
                .setParameter("eventId", eventId);
        User user = query.getSingleResult();
        return Optional.ofNullable(user);
    }

    @Override
    public Event update(Event eventInput, Long userId) {
        UserImpl userWithEvents = getUserImplWithEvents(userId);
        Optional<Event> optionalEventToBeModified = getEventFromUser(
                userWithEvents,
                eventInput);
        Event eventToBeModified = optionalEventToBeModified
                .orElseThrow(() -> new EntityNotFoundException());
        entityManager.detach(userWithEvents);
        eventToBeModified.setEventName(
                eventInput.getEventName());
        eventToBeModified.setSubject(
                eventInput.getSubject());
        eventToBeModified.setEventDescription(
                eventInput.getEventDescription());
        userWithEvents = entityManager
                .merge(userWithEvents);
        entityManager.flush();
        Optional<Event> optionalEventResponse = getEventFromUser(
                userWithEvents,
                eventToBeModified);
        return optionalEventResponse
                .orElseThrow(() -> new EntityNotFoundException());
    }
    @Override
    public Event addMember(Long eventId, Long memberId) {
        MemberImpl memberWithEvents = getMemberImplWithEventsIfAvailable(memberId);
        EventImpl eventWithMembers = getEventImplWithMembersIfAvailable(eventId);
        memberWithEvents.addEvent(eventWithMembers);
        entityManager.flush();
        return eventWithMembers;
    }
    private MemberImpl getMemberImplWithEventsIfAvailable(Long memberId) {
        Optional<Member> optionalMemberWithEvents = getMemberWithEvents(memberId);
        return (MemberImpl) optionalMemberWithEvents
                .orElseThrow(() -> new EntityNotFoundException());
    }
    private EventImpl getEventImplWithMembersIfAvailable(Long eventId) {
        Optional<Event> optionalEventWithMembers = getEventWithMembers(eventId);
        return (EventImpl) optionalEventWithMembers
                .orElseThrow(() -> new EntityNotFoundException());
    }
    @Override
    public Optional<Member> getMemberWithEvents(Long memberId) {
        TypedQuery<MemberImpl> query = entityManager.createQuery
                        ("SELECT m FROM Member m " +
                                "LEFT JOIN FETCH m.events " +
                                "WHERE m.memberId = :memberId", MemberImpl.class)
                .setParameter("memberId", memberId);
        MemberImpl memberImpl = query.getSingleResult();
        return Optional.ofNullable(memberImpl);
    }
    @Override
    public Optional<Event> getEventWithMembers(Long eventId) {
        TypedQuery<EventImpl> query = entityManager.createQuery
                        ("SELECT event FROM Event event " +
                                "LEFT JOIN FETCH event.members " +
                                "WHERE event.eventId = :eventId", EventImpl.class)
                .setParameter("eventId", eventId);
        EventImpl eventImpl = query.getSingleResult();
        return Optional.ofNullable(eventImpl);
    }
    @Override
    public void removeMember(Long eventId, Long memberId) {
        MemberImpl memberWithEvents = getMemberImplWithEventsIfAvailable(memberId);
        EventImpl eventWithMembers = getEventImplWithMembersIfAvailable(eventId);
        memberWithEvents.removeEvent(eventWithMembers);
        entityManager.flush();
    }
    @Override
    public void delete(Long eventId) {
        Optional<User> optionalUser = getPrimaryParent(eventId);
        User user = optionalUser
                .orElseThrow(() -> new EntityNotFoundException());
        Long userId = user.getUserId();
        UserImpl userWithEvents = getUserImplWithEvents(userId);
        Optional<Event> optionalEvent = get(eventId);
        EventImpl eventImpl = (EventImpl) optionalEvent
                .orElseThrow(() -> new EntityNotFoundException());
        userWithEvents.removeEvent(eventImpl);
        entityManager.flush();
    }
    public Boolean eventContainsMember(Long eventId, Long memberId) {
        EventImpl eventWithMembers = getEventImplWithMembersIfAvailable(eventId);
        MemberImpl memberImpl = entityManager
                .find(MemberImpl.class, memberId);
        Set<MemberImpl> memberImpls = eventWithMembers.getMembers();
        if(memberImpls.contains(memberImpl)) {
            return true;
        } else {
            return false;
        }
    }
}
