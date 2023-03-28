package com.knotslicer.server.ports.interactor;

import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Poll;
import com.knotslicer.server.domain.Schedule;
import java.util.Map;
import java.util.Set;

public interface FindEventTimesCommandCreator {
    InteractorCommand createFindEventTimesCommand(Map<Long, Schedule> schedules,
                                                  Set<Member> members,
                                                  Set<Poll> solutions,
                                                  EntityCreator entityCreator);
    InteractorCommandInvoker createCommandInvoker(InteractorCommand interactorCommand);
}
