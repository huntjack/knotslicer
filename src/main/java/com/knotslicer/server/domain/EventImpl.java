package com.knotslicer.server.domain;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity(name = "Event")
@Table(name = "Event")
public class EventImpl implements Event {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long eventId;

    @Column(unique=true, updatable = false, nullable = false)
    private String eventBusinessKey;
    private String subject;
    private String eventName;
    private String eventDescription;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private UserImpl user;
    @ManyToMany
    @JoinTable(name = "event_member",
                joinColumns = {@JoinColumn(name = "eventId")},
                inverseJoinColumns = {@JoinColumn(name = "memberId")})
    private Set<MemberImpl> members;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE}, orphanRemoval = true)
    private List<PollImpl> polls;

    public void addMember(MemberImpl member) {
        members.add(member);
        member.getEvents().add(this);
    }
    public void removeMember(MemberImpl member) {
        members.remove(member);
        member.getEvents().remove(this);
    }
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
