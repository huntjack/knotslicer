package com.knotslicer.server.domain;

import jakarta.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "PollAnswer")
@Table(name = "PollAnswer")
public class PollAnswerImpl implements PollAnswer {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(updatable = false, nullable = false)
    private Long pollAnswerId;
    @Column(unique=true, updatable = false, nullable = false)
    private String pollAnswerBusinessKey;
    private Boolean approved;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="pollId")
    private PollImpl poll;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="memberId")
    private MemberImpl member;

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
        PollAnswerImpl inputPollAnswer = (PollAnswerImpl) object;
        return Objects.equals(pollAnswerBusinessKey, inputPollAnswer.getPollAnswerBusinessKey());
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(pollAnswerBusinessKey);
    }
    public PollAnswerImpl() {this.pollAnswerBusinessKey = UUID.randomUUID().toString();}

    @Override
    public Long getPollAnswerId() {return pollAnswerId;}
    public String getPollAnswerBusinessKey() {return pollAnswerBusinessKey;}
    @Override
    public Boolean isApproved() {return approved;}
    @Override
    public void setApproved(Boolean approved) {this.approved = approved;}
    public PollImpl getPoll() {return poll;}
    public void setPoll(PollImpl poll) {this.poll = poll;}
    public MemberImpl getMember() {return member;}
    public void setMember(MemberImpl member) {this.member = member;}
}
