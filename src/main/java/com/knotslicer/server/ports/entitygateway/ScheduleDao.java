package com.knotslicer.server.ports.entitygateway;

import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Schedule;

public interface ScheduleDao extends ChildWithOneParentDao<Schedule, Member> {

}
