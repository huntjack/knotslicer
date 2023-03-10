package com.knotslicer.server.adapters.jpa;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@ProcessAs(ProcessType.MEMBER)
@ApplicationScoped
@Transactional(rollbackOn={Exception.class})
public class MemberDaoImpl implements ChildWithTwoParentsDao<Member, User, Project> {
    @PersistenceContext(unitName = "knotslicer_database")
    private EntityManager entityManager;

    @Override
    public Member create(Member member, Long userId, Long projectId) {
        MemberImpl memberImpl = (MemberImpl) member;
        Optional<User> optionalUserWithMembers = getPrimaryParentWithChildren(userId);
        UserImpl userWithMembers = (UserImpl) optionalUserWithMembers
                .orElseThrow(() -> new EntityNotFoundException());
        entityManager.detach(userWithMembers);
        userWithMembers.addMember(memberImpl);
        userWithMembers = entityManager.merge(userWithMembers);
        Optional<Project> optionalProjectWithMembers = getSecondaryParentWithChildren(projectId);
        ProjectImpl projectWithMembers = (ProjectImpl) optionalProjectWithMembers
                .orElseThrow(() -> new EntityNotFoundException());
        Optional<MemberImpl> optionalMemberImpl =
                getMemberFromUser(userWithMembers, memberImpl);
        memberImpl = optionalMemberImpl
                .orElseThrow(() -> new EntityNotFoundException());
        projectWithMembers.addMember(memberImpl);
        entityManager.flush();
        return memberImpl;
    }
    private Optional<MemberImpl> getMemberFromUser(UserImpl userImpl, Member member) {
        List<MemberImpl> memberImpls = userImpl.getMembers();
        int memberIndex = memberImpls.indexOf(member);
        MemberImpl memberImpl = memberImpls.get(memberIndex);
        return Optional.ofNullable(memberImpl);
    }
    @Override
    public Optional<Member> get(Long memberId) {
        Member member = entityManager.find(MemberImpl.class, memberId);
        return Optional.ofNullable(member);
    }
    @Override
    public Optional<User> getPrimaryParentWithChildren(Long userId) {
        TypedQuery<UserImpl> query = entityManager.createQuery
                        ("SELECT user FROM User user " +
                                "LEFT JOIN FETCH user.members " +
                                "WHERE user.userId = :userId", UserImpl.class)
                .setParameter("userId", userId);
        User user = query.getSingleResult();
        return Optional.ofNullable(user);
    }
    @Override
    public Optional<Project> getSecondaryParentWithChildren(Long projectId) {
        TypedQuery<ProjectImpl> query = entityManager.createQuery
                        ("SELECT project FROM Project project " +
                                "LEFT JOIN FETCH project.members " +
                                "WHERE project.projectId = :projectId", ProjectImpl.class)
                .setParameter("projectId", projectId);
        Project project = query.getSingleResult();
        return Optional.ofNullable(project);
    }
    @Override
    public Optional<User> getPrimaryParent(Long memberId) {
        TypedQuery<UserImpl> query = entityManager.createQuery(
                "SELECT user FROM User user " +
                        "INNER JOIN user.members m " +
                        "WHERE m.memberId = :memberId", UserImpl.class)
                .setParameter("memberId", memberId);
        User user = query.getSingleResult();
        return Optional.ofNullable(user);
    }
    @Override
    public Optional<Project> getSecondaryParent(Long memberId) {
        TypedQuery<ProjectImpl> query = entityManager.createQuery(
                "SELECT project FROM Project project " +
                        "INNER JOIN project.members m " +
                        "WHERE m.memberId = :memberId", ProjectImpl.class)
                .setParameter("memberId", memberId);
        Project project = query.getSingleResult();
        return Optional.ofNullable(project);
    }
    @Override
    public Member update(Member memberInput, Long userId) {
        Optional<User> optionalUserWithMembers = getPrimaryParentWithChildren(userId);
        UserImpl userWithMembers = (UserImpl) optionalUserWithMembers
                .orElseThrow(() -> new EntityNotFoundException());
        Optional<MemberImpl> optionalMemberToBeModified =
                getMemberFromUser(
                        userWithMembers,
                        memberInput);
        Member memberToBeModified = optionalMemberToBeModified
                .orElseThrow(() -> new EntityNotFoundException());
        entityManager.detach(userWithMembers);
        memberToBeModified.setName(
                memberInput.getName());
        memberToBeModified.setRole(
                memberInput.getRole());
        memberToBeModified.setRoleDescription(
                memberInput.getRoleDescription());
        userWithMembers = entityManager.merge(userWithMembers);
        entityManager.flush();
        Optional<MemberImpl> optionalMemberResponse = getMemberFromUser(
                userWithMembers,
                memberToBeModified);
        return optionalMemberResponse
                .orElseThrow(() -> new EntityNotFoundException());
    }

    @Override
    public void delete(Long memberId) {
        Optional<User> optionalUser = getPrimaryParent(memberId);
        User user = optionalUser
                .orElseThrow(() -> new EntityNotFoundException());
        Optional<User> optionalUserWithMembers =
                getPrimaryParentWithChildren(
                        user.getUserId());
        UserImpl userWithMembers = (UserImpl) optionalUserWithMembers
                .orElseThrow(() -> new EntityNotFoundException());
        Optional<Member> optionalMember = get(memberId);
        MemberImpl memberImpl = (MemberImpl) optionalMember
                .orElseThrow(() -> new EntityNotFoundException());
        Set<EventImpl> events =
                new HashSet<>(memberImpl.getEvents());
        for(EventImpl event: events) {
            memberImpl.removeEvent(event);
        }
        userWithMembers.removeMember(memberImpl);
        entityManager.flush();
    }
}
