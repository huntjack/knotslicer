package com.knotslicer.server.entities;

import jakarta.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Event {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(unique = true, updatable = false, nullable = false)
    private Long eventId;
    @Column(unique=true, updatable = false, nullable = false)
    private String eventBusinessKey;
    private String subject;
    private String eventName;
    private String eventDescription;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="userId")
    private User user;
    @ManyToMany
    @JoinTable(name = "event_member",
                joinColumns = {@JoinColumn(name = "eventId")},
                inverseJoinColumns = {@JoinColumn(name = "memberId")})
    private Set<Member> members;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "event", orphanRemoval = true)
    private List<Poll> polls;

    public void addMember(Member member) {
        members.add(member);
        member.getEvents().add(this);
    }
    public void removeMember(Member member) {
        members.remove(member);
        member.getEvents().remove(this);
    }
    public void addPoll(Poll poll) {
        poll.setEvent(this);
        polls.add(poll);
    }
    public void removePoll(Poll poll) {
        polls.remove(poll);
        poll.setEvent(null);
    }

    public Event() {}
    public Event(String eventBusinessKey, String subject, String eventName, String eventDescription) {
        this.eventBusinessKey = eventBusinessKey;
        this.subject = subject;
        this.eventName = eventName;
        this.eventDescription = eventDescription;
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
        Event inputEvent = (Event) object;
        return Objects.equals(eventBusinessKey, inputEvent.getEventBusinessKey());
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(eventBusinessKey);
    }

    public String getEventBusinessKey() {return eventBusinessKey;}
    public void setEventBusinessKey(String eventBusinessKey) {this.eventBusinessKey = eventBusinessKey;}
    public String getSubject() {return subject;}
    public void setSubject(String subject) {this.subject = subject;}
    public String getEventName() {return eventName;}
    public void setEventName(String eventName) {this.eventName = eventName;}
    public String getEventDescription() {return eventDescription;}
    public void setEventDescription(String eventDescription) {this.eventDescription = eventDescription;}

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}
    public Set<Member> getMembers() {return members;}
    public void setMembers(Set<Member> members) {this.members = members;}
    public List<Poll> getPolls() {return polls;}
    public void setPolls(List<Poll> polls) {this.polls = polls;}
}
