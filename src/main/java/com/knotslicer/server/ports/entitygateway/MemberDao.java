package com.knotslicer.server.ports.entitygateway;

import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Project;
import com.knotslicer.server.domain.User;

public interface MemberDao extends ChildWithTwoParentsDao<Member, User, Project> {
    Member getWithEvents(Long memberId);
}
