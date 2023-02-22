package com.knotslicer.server.ports.entitygateway;

import com.knotslicer.server.domain.Event;
import com.knotslicer.server.domain.User;

public interface EventDao extends ChildWithOneRequiredParentDao<Event, User> {

}
