package com.knotslicer.server.adapters.jpa;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@ProcessAs(ProcessType.MEMBER)
@ApplicationScoped
@Transactional(rollbackOn={Exception.class})
public class MemberDaoImpl implements ChildWithTwoParentsDao<Member,User,Project> {
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
        return memberImpl;
    }
    private UserImpl getUserWithMembersFromJpa(Long userId) {
        TypedQuery<UserImpl> query = entityManager.createQuery
                        ("SELECT user FROM User user " +
                                "LEFT JOIN FETCH user.members " +
                                "WHERE user.userId = :userId", UserImpl.class)
                .setParameter("userId", userId);
        return query.getSingleResult();
    }
    private ProjectImpl getProjectWithMembersFromJpa(Long projectId) {
        TypedQuery<ProjectImpl> query = entityManager.createQuery
                        ("SELECT project FROM Project project " +
                                "LEFT JOIN FETCH project.members " +
                                "WHERE project.projectId = :projectId", ProjectImpl.class)
                .setParameter("projectId", projectId);
        return query.getSingleResult();
    }
    private MemberImpl getMemberFromUser(UserImpl userImpl, Member member) {
        List<MemberImpl> memberImpls = userImpl.getMembers();
        int memberIndex = memberImpls.indexOf(member);
        return memberImpls.get(memberIndex);
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
    public Optional<Project> getSecondaryParentWithChildren(Long projectId) {
        Project project = getProjectWithMembersFromJpa(projectId);
        return Optional.ofNullable(project);
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
    @Override
    public Member update(Member memberInput, Long userId) {
        UserImpl userImpl = getUserWithMembersFromJpa(userId);
        entityManager.detach(userImpl);
        Member memberToBeModified =
                getMemberFromUser(
                        userImpl,
                        memberInput);
        memberToBeModified.setName(
                memberInput.getName());
        memberToBeModified.setRole(
                memberInput.getRole());
        memberToBeModified.setRoleDescription(
                memberInput.getRoleDescription());
        userImpl = entityManager.merge(userImpl);
        entityManager.flush();
        return getMemberFromUser(
                userImpl,
                memberToBeModified);
    }

    @Override
    public void delete(Long memberId, Long userId) {
        UserImpl userImpl = getUserWithMembersFromJpa(userId);
        MemberImpl memberImpl = entityManager.find(MemberImpl.class, memberId);
        userImpl.removeMember(memberImpl);
        entityManager.flush();
    }
}
