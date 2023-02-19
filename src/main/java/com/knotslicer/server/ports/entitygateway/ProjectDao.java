package com.knotslicer.server.ports.entitygateway;

import com.knotslicer.server.domain.Project;
import com.knotslicer.server.domain.User;

public interface ProjectDao extends ChildWithOneRequiredParentDao<Project, User> {

}
