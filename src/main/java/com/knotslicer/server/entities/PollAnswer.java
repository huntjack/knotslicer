package com.knotslicer.server.entities;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
public class PollAnswer {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(unique = true, updatable = false, nullable = false)
    private Long pollAnswerId;
    @Column(unique=true, updatable = false, nullable = false)
    private String pollAnswerBusinessKey;
    private boolean approved;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="pollId")
    private Poll poll;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="memberId")
    private Member member;

    public PollAnswer() {}
    public PollAnswer(String pollAnswerBusinessKey, boolean approved) {
        this.pollAnswerBusinessKey = pollAnswerBusinessKey;
        this.approved = approved;
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
        PollAnswer inputPollAnswer = (PollAnswer) object;
        return Objects.equals(pollAnswerBusinessKey, inputPollAnswer.getPollAnswerBusinessKey());
    }
    @Override
    public int hashCode() {
        return Objects.hashCode(pollAnswerBusinessKey);
    }
    public String getPollAnswerBusinessKey() {return pollAnswerBusinessKey;}
    public void setPollAnswerBusinessKey(String pollAnswerBusinessKey) {this.pollAnswerBusinessKey = pollAnswerBusinessKey;}
    public boolean isApproved() {return approved;}
    public void setApproved(boolean approved) {this.approved = approved;}
    public Poll getPoll() {return poll;}
    public void setPoll(Poll poll) {this.poll = poll;}
    public Member getMember() {return member;}
    public void setMember(Member member) {this.member = member;}
}
