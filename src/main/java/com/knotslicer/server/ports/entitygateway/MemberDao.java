package com.knotslicer.server.ports.entitygateway;

import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Project;

import java.util.Optional;

public interface MemberDao {
    Member createMember(Member member, Long userId);
    Optional<Member> getMember(Long memberId);
    Optional<Project> getProjectWithMembers(Long projectId);
    Member updateMember(Member inputMember, Long userId);
    void deleteMember(Long memberId, Long userId);
}
