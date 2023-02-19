package com.knotslicer.server.adapters.jpa;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.MemberDao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Transactional(rollbackOn={Exception.class})
public class MemberDaoImpl implements MemberDao {
    @PersistenceContext(unitName = "knotslicer_database")
    private EntityManager entityManager;

    @Override
    public Member create(Member member, Long userId, Long projectId) {
        MemberImpl memberImpl = (MemberImpl) member;
        UserImpl userImpl = getUserWithMembersFromJpa(userId);
        entityManager.detach(userImpl);
        userImpl.addMember(memberImpl);
        userImpl = entityManager.merge(userImpl);
        ProjectImpl projectImpl = getProjectWithMembersFromJpa(projectId);
        memberImpl = getMemberFromUser(userImpl, memberImpl);
        projectImpl.addMember(memberImpl);
        entityManager.flush();
        entityManager.refresh(memberImpl);
        return memberImpl;
    }
    private UserImpl getUserWithMembersFromJpa(Long userId) {
        TypedQuery<UserImpl> query = entityManager.createQuery
                        ("SELECT user FROM User user " +
                                "INNER JOIN FETCH user.members " +
                                "WHERE user.userId = :userId", UserImpl.class)
                .setParameter("userId", userId);
        return query.getSingleResult();
    }
    private ProjectImpl getProjectWithMembersFromJpa(Long projectId) {
        TypedQuery<ProjectImpl> query = entityManager.createQuery
                        ("SELECT project FROM Project project " +
                                "INNER JOIN FETCH project.members " +
                                "WHERE project.projectId = :projectId", ProjectImpl.class)
                .setParameter("projectId", projectId);
        return query.getSingleResult();
    }
    private MemberImpl getMemberFromUser(UserImpl userImpl, Member member) {
        List<MemberImpl> memberList = userImpl.getMembers();
        int memberIndex = memberList.indexOf(member);
        return memberList.get(memberIndex);
    }
    @Override
    public Optional<Member> get(Long memberId) {
        Member member = entityManager.find(MemberImpl.class, memberId);
        return Optional.ofNullable(member);
    }
    @Override
    public Optional<User> getPrimaryParentWithChildren(Long userId) {
        User user = getUserWithMembersFromJpa(userId);
        return Optional.ofNullable(user);
    }
    @Override
    public Optional<Project> getProjectWithMembers(Long projectId) {
        Project project = getProjectWithMembersFromJpa(projectId);
        return Optional.ofNullable(project);
    }

    @Override
    public Member update(Member inputMember, Long userId) {
        UserImpl userImpl = getUserWithMembersFromJpa(userId);
        entityManager.detach(userImpl);
        Member memberToBeModified =
                getMemberFromUser(
                        userImpl,
                        inputMember);
        memberToBeModified
                .setName(
                inputMember.getName());
        memberToBeModified
                .setRole(
                        inputMember.getRole());
        memberToBeModified
                .setRoleDescription(
                        inputMember.getRoleDescription());
        userImpl = entityManager.merge(userImpl);
        entityManager.flush();
        Member updatedMember = getMemberFromUser(userImpl, memberToBeModified);
        return updatedMember;
    }

    @Override
    public void delete(Long memberId, Long userId) {
        UserImpl userImpl = getUserWithMembersFromJpa(userId);
        MemberImpl memberImpl = entityManager.find(MemberImpl.class, memberId);
        userImpl.removeMember(memberImpl);
        entityManager.flush();
    }

    @Override
    public Long getPrimaryParentId(Long memberId) {
        TypedQuery<UserImpl> query = entityManager.createQuery
                        ("SELECT user FROM User user " +
                                "INNER JOIN user.members m " +
                                "WHERE m.memberId = :memberId", UserImpl.class)
                .setParameter("memberId", memberId);
        User user = query.getSingleResult();
        return user.getUserId();
    }
    @Override
    public Long getSecondaryParentId(Long memberId) {
        TypedQuery<ProjectImpl> query = entityManager.createQuery
                        ("SELECT project FROM Project project " +
                                "INNER JOIN project.members m " +
                                "WHERE m.memberId = :memberId", ProjectImpl.class)
                .setParameter("memberId", memberId);
        Project project = query.getSingleResult();
        return project.getProjectId();
    }
}
