package com.knotslicer.server.ports.interactor;

import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Schedule;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.Map;
import java.util.Set;

@ApplicationScoped
public class FindEventTimesCommandCreatorImpl implements FindEventTimesCommandCreator {
    @Override
    public FindEventTimesCommand createFindEventTimesCommand(Map<Long, Schedule> schedules,
                                                             Set<Member> members,
                                                             Long minimumMeetingTimeInMinutes,
                                                             EntityCreator entityCreator) {
        return new FindEventTimesCommandImpl(schedules, members, minimumMeetingTimeInMinutes, entityCreator);
    }
    @Override
    public FindEventTimesCommandInvoker createCommandInvoker(FindEventTimesCommand findEventTimesCommand) {
        return new FindEventTimesCommandInvokerImpl(findEventTimesCommand);
    }
}
