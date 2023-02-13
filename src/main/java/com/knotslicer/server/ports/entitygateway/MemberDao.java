package com.knotslicer.server.ports.entitygateway;

import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Project;
import com.knotslicer.server.domain.User;
import java.util.Optional;

public interface MemberDao extends ChildWithTwoParentsDao<Member, User> {
    Optional<Project> getProjectWithMembers(Long projectId);
}
