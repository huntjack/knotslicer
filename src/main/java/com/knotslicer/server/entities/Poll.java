package com.knotslicer.server.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class Poll {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(unique = true, updatable = false, nullable = false)
    private Long pollId;
    @Column(unique=true, updatable = false, nullable = false)
    private String pollBusinessKey;
    private LocalDateTime startTimeUtc;
    private LocalDateTime endTimeUtc;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="eventId")
    private Event event;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "poll", orphanRemoval = true)
    private List<PollAnswer> pollAnswers;
    public void addPollAnswer(PollAnswer pollAnswer) {
        pollAnswer.setPoll(this);
        pollAnswers.add(pollAnswer);
    }
    public void removePollAnswer(PollAnswer pollAnswer) {
        pollAnswers.remove(pollAnswer);
        pollAnswer.setPoll(null);
    }

    public Poll() {}
    public Poll(String pollBusinessKey, LocalDateTime startTimeUtc, LocalDateTime endTimeUtc) {
        this.pollBusinessKey = pollBusinessKey;
        this.startTimeUtc = startTimeUtc;
        this.endTimeUtc = endTimeUtc;
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
        Poll inputPoll = (Poll)object;
        return Objects.equals(pollBusinessKey, inputPoll.getPollBusinessKey());
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(pollBusinessKey);
    }

    public String getPollBusinessKey() {return pollBusinessKey;}
    public void setPollBusinessKey(String pollBusinessKey) {this.pollBusinessKey = pollBusinessKey;}
    public LocalDateTime getStartTimeUtc() {return startTimeUtc;}
    public void setStartTimeUtc(LocalDateTime startTimeUtc) {this.startTimeUtc = startTimeUtc;}
    public LocalDateTime getEndTimeUtc() {return endTimeUtc;}
    public void setEndTimeUtc(LocalDateTime endTimeUtc) {this.endTimeUtc = endTimeUtc;}
    public Event getEvent() {return event;}
    public void setEvent(Event event) {this.event = event;}
    public List<PollAnswer> getPollAnswers() {return pollAnswers;}
    public void setPollAnswers(List<PollAnswer> pollAnswers) {this.pollAnswers = pollAnswers;}
}
