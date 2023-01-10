package com.knotslicer.server.control;

import com.knotslicer.server.entity.*;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
@JpaEntity
public class JpaEntityFactory implements AbstractEntityFactory {
    @Override
    public User createUser() {return new UserImpl();}
    @Override
    public Project createProject() {return new ProjectImpl();}
    @Override
    public Member createMember() {
        return new MemberImpl();
    }
    @Override
    public Event createEvent() {
        return new EventImpl();
    }
    @Override
    public Schedule createSchedule() {
        return new ScheduleImpl();
    }
    @Override
    public Poll createPoll() {
        return new PollImpl();
    }
    @Override
    public PollAnswer createPollAnswer() {
        return new PollAnswerImpl();
    }
}
