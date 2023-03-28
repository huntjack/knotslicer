package com.knotslicer.server.ports.interactor;

import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Poll;
import com.knotslicer.server.domain.Schedule;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class FindEventTimesCommandCreatorImpl implements FindEventTimesCommandCreator {
    @Override
    public InteractorCommand createFindEventTimesCommand(Map<Long, Schedule> schedules,
                                                         Set<Member> members,
                                                         Set<Poll> solutions,
                                                         EntityCreator entityCreator) {
        return new FindEventTimesCommand(schedules, members, solutions, entityCreator);
    }
    @Override
    public InteractorCommandInvoker createCommandInvoker(InteractorCommand interactorCommand) {
        return new InteractorCommandInvokerImpl(interactorCommand);
    }
}
