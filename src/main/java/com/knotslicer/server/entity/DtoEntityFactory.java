package com.knotslicer.server.entity;

public class DtoEntityFactory implements AbstractEntityFactory{
    @Override
    public User createUser() {
        return new UserDtoImpl();
    }

    @Override
    public Project createProject() {
        return new ProjectDtoImpl();
    }

    @Override
    public Member createMember() {
        return new MemberDtoImpl();
    }

    @Override
    public Event createEvent() {
        return new EventDtoImpl();
    }

    @Override
    public Schedule createSchedule() {
        return new ScheduleDtoImpl();
    }

    @Override
    public Poll createPoll() {
        return new PollDtoImpl();
    }

    @Override
    public PollAnswer createPollAnswer() {
        return new PollAnswerDtoImpl();
    }
}
