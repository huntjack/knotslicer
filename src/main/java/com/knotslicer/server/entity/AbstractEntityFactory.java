package com.knotslicer.server.entity;

public interface AbstractEntityFactory {
    public User createUser();
    public Project createProject();
    public Member createMember();
    public Event createEvent();
    public Schedule createSchedule();
    public Poll createPoll();
    public PollAnswer createPollAnswer();
}
