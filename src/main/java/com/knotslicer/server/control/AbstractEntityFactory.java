package com.knotslicer.server.control;

import com.knotslicer.server.entity.*;

public interface AbstractEntityFactory {
    User createUser();
    Project createProject();
    Member createMember();
    Event createEvent();
    Schedule createSchedule();
    Poll createPoll();
    PollAnswer createPollAnswer();
}
