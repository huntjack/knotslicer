package com.knotslicer.server.entity;

import java.util.UUID;

public class JpaEntityFactory implements AbstractEntityFactory {
    @Override
    public User createUser() {
        return new UserImpl(UUID.randomUUID().toString());
    }

    @Override
    public Project createProject() {
        return new ProjectImpl(UUID.randomUUID().toString());
    }

    @Override
    public Member createMember() {
        return new MemberImpl(UUID.randomUUID().toString());
    }

    @Override
    public Event createEvent() {
        return new EventImpl(UUID.randomUUID().toString());
    }

    @Override
    public Schedule createSchedule() {
        return new ScheduleImpl(UUID.randomUUID().toString());
    }

    @Override
    public Poll createPoll() {
        return new PollImpl(UUID.randomUUID().toString());
    }

    @Override
    public PollAnswer createPollAnswer() {
        return new PollAnswerImpl(UUID.randomUUID().toString());
    }
}
