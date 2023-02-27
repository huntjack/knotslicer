package com.knotslicer.server.ports.interactor.datatransferobjects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(as = PollAnswerDtoImpl.class)
public interface PollAnswerDto extends Linkable {
    Long getPollId();
    void setPollId(Long pollId);
    Long getMemberId();
    void setMemberId(Long memberId);
    Long getPollAnswerId();
    void setPollAnswerId(Long pollAnswerId);
    Boolean isApproved();
    void setApproved(Boolean approved);
    List<Link> getLinks();
    void setLinks(List<Link> links);
}
