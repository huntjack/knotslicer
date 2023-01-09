package com.knotslicer.server.entity;


import java.io.Serializable;

public class PollAnswerDtoImpl implements PollAnswer, Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean approved;
    @Override
    public Boolean isApproved() {
        return approved;
    }
    @Override
    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}
