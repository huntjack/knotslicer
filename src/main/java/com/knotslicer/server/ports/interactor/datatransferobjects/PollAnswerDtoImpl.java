package com.knotslicer.server.ports.interactor.datatransferobjects;


import java.io.Serializable;

public class PollAnswerDtoImpl implements PollAnswerDto, Serializable {
    private static final long serialVersionUID = 8000L;
    private Long pollId;
    private Long pollAnswerId;
    private Boolean approved;

    @Override
    public Long getPollId() {return pollId;}
    @Override
    public void setPollId(Long pollId) {this.pollId = pollId;}
    @Override
    public Long getPollAnswerId() {return pollAnswerId;}
    @Override
    public void setPollAnswerId(Long pollAnswerId) {this.pollAnswerId = pollAnswerId;}
    @Override
    public Boolean isApproved() {
        return approved;
    }
    @Override
    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}
