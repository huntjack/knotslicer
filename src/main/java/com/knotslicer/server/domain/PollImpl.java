package com.knotslicer.server.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "Poll")
@Table(name = "Poll")
public class PollImpl implements Poll {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(updatable = false)
    private Long pollId;
    @Column(unique=true, updatable = false, nullable = false)
    private String pollBusinessKey;
    private LocalDateTime startTimeUtc;
    private LocalDateTime endTimeUtc;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="eventId")
    private EventImpl event;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "poll", cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE}, orphanRemoval = true)
    private List<PollAnswerImpl> pollAnswers = new ArrayList<>();
    public void addPollAnswer(PollAnswerImpl pollAnswer) {
        pollAnswer.setPoll(this);
        pollAnswers.add(pollAnswer);
    }
    public void removePollAnswer(PollAnswerImpl pollAnswer) {
        pollAnswers.remove(pollAnswer);
        pollAnswer.setPoll(null);
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
        PollImpl inputPoll = (PollImpl)object;
        return Objects.equals(pollBusinessKey, inputPoll.getPollBusinessKey());
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(pollBusinessKey);
    }

    public PollImpl() {this.pollBusinessKey = UUID.randomUUID().toString();}

    @Override
    public Long getPollId() {return pollId;}
    public String getPollBusinessKey() {return pollBusinessKey;}
    @Override
    public LocalDateTime getStartTimeUtc() {return startTimeUtc;}
    @Override
    public void setStartTimeUtc(LocalDateTime startTimeUtc) {this.startTimeUtc = startTimeUtc;}
    @Override
    public LocalDateTime getEndTimeUtc() {return endTimeUtc;}
    @Override
    public void setEndTimeUtc(LocalDateTime endTimeUtc) {this.endTimeUtc = endTimeUtc;}
    public EventImpl getEvent() {return event;}
    public void setEvent(EventImpl event) {this.event = event;}
    public List<PollAnswerImpl> getPollAnswers() {return pollAnswers;}
    public void setPollAnswers(List<PollAnswerImpl> pollAnswers) {this.pollAnswers = pollAnswers;}
}
