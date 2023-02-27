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

@ProcessAs(ProcessType.EVENT)
@ApplicationScoped
@Transactional(rollbackOn={Exception.class})
public class EventDaoImpl implements ChildWithOneRequiredParentDao<Event, User> {
    @PersistenceContext(unitName = "knotslicer_database")
    private EntityManager entityManager;

    @Override
    public Event create(Event event, Long userId) {
        UserImpl userImpl = getUserWithEventsFromJpa(userId);
        entityManager.detach(userImpl);
        userImpl.addEvent((EventImpl) event);
        userImpl = entityManager.merge(userImpl);
        entityManager.flush();
        event = getEventFromUser(userImpl, event);
        entityManager.refresh(event);
        return event;
    }
    private UserImpl getUserWithEventsFromJpa(Long userId) {
        TypedQuery<UserImpl> query = entityManager.createQuery
                        ("SELECT user FROM User user " +
                                "INNER JOIN FETCH user.events " +
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
    public Long getPrimaryParentId(Long eventId) {
        TypedQuery<UserImpl> query = entityManager.createQuery
                        ("SELECT user FROM User user " +
                                "INNER JOIN user.events event " +
                                "WHERE event.eventId = :eventId", UserImpl.class)
                .setParameter("eventId", eventId);
        User user = query.getSingleResult();
        return user.getUserId();
    }
    @Override
    public Optional<User> getPrimaryParentWithChildren(Long userId) {
        User user = getUserWithEventsFromJpa(userId);
        return Optional.ofNullable(user);
    }
    @Override
    public Event update(Event eventInput, Long userId) {
        UserImpl userImpl = getUserWithEventsFromJpa(userId);
        entityManager.detach(userImpl);
        Event eventToBeModified = getEventFromUser(userImpl, eventInput);
        eventToBeModified.setEventName(
                eventInput.getEventName());
        eventToBeModified.setSubject(
                eventInput.getSubject());
        eventToBeModified.setEventDescription(
                eventInput.getEventDescription());
        userImpl = entityManager
                .merge(userImpl);
        entityManager.flush();
        Event updatedEvent =
                getEventFromUser(userImpl, eventToBeModified);
        return updatedEvent;
    }

    @Override
    public void delete(Long eventId, Long userId) {
        UserImpl userImpl = getUserWithEventsFromJpa(userId);
        EventImpl eventImpl = entityManager.find(EventImpl.class, eventId);
        userImpl.removeEvent(eventImpl);
        entityManager.flush();
    }
}
