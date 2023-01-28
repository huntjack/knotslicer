package com.knotslicer.server.ports.interactor;

import com.knotslicer.server.domain.*;

public interface EntityFactory {
    User createUser();
    Project createProject();
    Member createMember();
    Event createEvent();
    Schedule createSchedule();
    Poll createPoll();
    PollAnswer createPollAnswer();
}
