package com.knotslicer.server.ports.entitygateway;

import com.knotslicer.server.domain.Event;
import com.knotslicer.server.domain.Poll;

public interface PollDao extends ChildWithOneRequiredParentDao<Poll, Event> {
    
}
