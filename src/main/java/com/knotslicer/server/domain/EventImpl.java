package com.knotslicer.server.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.*;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity(name = "Event")
@Table(name = "Event")
@NamedQuery(name = "getEventWithPolls",
        query = "SELECT event FROM Event event " +
                "LEFT JOIN FETCH event.polls " +
                "WHERE event.eventId = :eventId")
public class EventImpl implements Event {
    @Id
    @SequenceGenerator(name="event_generator", sequenceName = "event_sequence", allocationSize=1)
    @GeneratedValue(strategy=SEQUENCE, generator="event_generator")
    @Column(updatable = false, nullable = false)
    private Long eventId;
    @Column(unique=true, updatable = false, nullable = false)
    private String eventBusinessKey;
    @Size(max=100)
    @NotBlank
    private String subject;
    @Size(max=50)
    @NotBlank
    private String eventName;
    @Size(min=8, max=250)
    @NotBlank
    private String eventDescription;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private UserImpl user;
    @ManyToMany
    @JoinTable(name = "Event_Member",
                joinColumns = {@JoinColumn(name = "eventId")},
                inverseJoinColumns = {@JoinColumn(name = "memberId")})
    private Set<MemberImpl> members = new HashSet<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE}, orphanRemoval = true)
    private List<PollImpl> polls = new ArrayList<>();

    public void addPoll(PollImpl poll) {
        poll.setEvent(this);
        polls.add(poll);
    }
    public void removePoll(PollImpl poll) {
        polls.remove(poll);
        poll.setEvent(null);
    }

    @Override
    public boolean equals(Object object) {
        if(this==object){
            return true;
        }
        if(object==null) {
            return false;
        }
        if(getClass() != object.getClass()) {
            return false;
        }
        EventImpl inputEvent = (EventImpl) object;
        return Objects.equals(eventBusinessKey, inputEvent.getEventBusinessKey());
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(eventBusinessKey);
    }
    public EventImpl() {this.eventBusinessKey = UUID.randomUUID().toString();}

    @Override
    public Long getEventId() {return eventId;}
    @Override
    public void setEventId(Long eventId) {this.eventId = eventId;}

    public String getEventBusinessKey() {return eventBusinessKey;}
    @Override
    public String getSubject() {return subject;}
    @Override
    public void setSubject(String subject) {this.subject = subject;}
    @Override
    public String getEventName() {return eventName;}
    @Override
    public void setEventName(String eventName) {this.eventName = eventName;}
    @Override
    public String getEventDescription() {return eventDescription;}
    @Override
    public void setEventDescription(String eventDescription) {this.eventDescription = eventDescription;}

    public UserImpl getUser() {return user;}
    public void setUser(UserImpl user) {this.user = user;}
    public Set<MemberImpl> getMembers() {return members;}
    public void setMembers(Set<MemberImpl> members) {this.members = members;}
    public List<PollImpl> getPolls() {return polls;}
    public void setPolls(List<PollImpl> polls) {this.polls = polls;}
}
