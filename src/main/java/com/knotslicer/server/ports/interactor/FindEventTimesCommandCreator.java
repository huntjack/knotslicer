package com.knotslicer.server.ports.interactor;

import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Schedule;
import java.util.Map;
import java.util.Set;

public interface FindEventTimesCommandCreator {
    FindEventTimesCommand createFindEventTimesCommand(Map<Long, Schedule> schedules,
                                                      Set<Member> members,
                                                      EntityCreator entityCreator);
    FindEventTimesCommandInvoker createCommandInvoker(FindEventTimesCommand findEventTimesCommand);
}
