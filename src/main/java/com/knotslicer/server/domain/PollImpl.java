package com.knotslicer.server.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity(name = "Poll")
@Table(name = "Poll")
public class PollImpl implements Poll {
    @Id
    @SequenceGenerator(name="poll_generator", sequenceName = "poll_sequence", allocationSize=1)
    @GeneratedValue(strategy=SEQUENCE, generator="poll_generator")
    @Column(updatable = false, nullable = false)
    private Long pollId;
    @Column(unique=true, updatable = false, nullable = false)
    private String pollBusinessKey;
    @Future
    private LocalDateTime startTimeUtc;
    @Future
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
    public PollImpl(PollImpl pollImpl) {
        this.pollId = pollImpl.getPollId();
        this.pollBusinessKey = pollImpl.getPollBusinessKey();
        this.startTimeUtc = pollImpl.getStartTimeUtc();
        this.endTimeUtc = pollImpl.getEndTimeUtc();
        this.event = pollImpl.getEvent();
        this.pollAnswers = pollImpl.getPollAnswers();
    }
    @Override
    public Long getPollId() {return pollId;}
    @Override
    public void setPollId(Long pollId) {this.pollId = pollId;}
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
