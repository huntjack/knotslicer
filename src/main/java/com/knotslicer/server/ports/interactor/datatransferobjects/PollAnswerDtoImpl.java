package com.knotslicer.server.ports.interactor.datatransferobjects;


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class PollAnswerDtoImpl implements PollAnswerDto, Serializable {
    private static final long serialVersionUID = 9000L;
    private Long pollId;
    private Long memberId;
    private Long pollAnswerId;
    private Boolean approved;
    private List<Link> links = new LinkedList<>();
    @Override
    public void addLink(String url, String rel) {
        Link link = createLink();
        link.setLink(url);
        link.setRel(rel);
        links.add(link);
    }
    private Link createLink() {
        return new LinkImpl();
    }
    @Override
    public Long getPollId() {return pollId;}
    @Override
    public void setPollId(Long pollId) {this.pollId = pollId;}
    @Override
    public Long getMemberId() {return memberId;}
    @Override
    public void setMemberId(Long memberId) {this.memberId = memberId;}
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
    @Override
    public List<Link> getLinks() {return links;}
}
