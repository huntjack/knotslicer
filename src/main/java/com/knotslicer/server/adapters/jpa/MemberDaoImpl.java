package com.knotslicer.server.adapters.jpa;

import com.knotslicer.server.domain.*;
import com.knotslicer.server.ports.entitygateway.MemberDao;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
@Transactional(rollbackOn={Exception.class})
public class MemberDaoImpl implements MemberDao {
    @PersistenceContext(unitName = "knotslicer_database")
    EntityManager entityManager;
    @Override
    public Member createMember(Member member, Long userId) {
        UserImpl userImpl = getUserWithMembersFromJpa(userId);
        entityManager.detach(userImpl);
        userImpl.addMember((MemberImpl) member);
        userImpl = entityManager.merge(userImpl);
        entityManager.flush();
        int memberIndex = userImpl
                .getMembers()
                .indexOf(member);
        return userImpl
                .getMembers()
                .get(memberIndex);
    }
    private UserImpl getUserWithMembersFromJpa(Long userId) {
        TypedQuery<UserImpl> query = entityManager.createQuery
                        ("SELECT user FROM User user " +
                                "INNER JOIN FETCH user.members " +
                                "WHERE user.userId = :userId", UserImpl.class)
                .setParameter("userId", userId);
        return query.getSingleResult();
    }

    @Override
    public Optional<Member> getMember(Long memberId) {
        return Optional.empty();
    }
    @Override
    public Optional<Project> getProjectWithMembers(Long projectId) {
        Project project = getProjectWithMembersFromJpa(projectId);
        return Optional.ofNullable(project);
    }
    private ProjectImpl getProjectWithMembersFromJpa(Long projectId) {
        TypedQuery<ProjectImpl> query = entityManager.createQuery
                        ("SELECT project FROM Project project " +
                                "INNER JOIN FETCH project.members " +
                                "WHERE project.projectId = :projectId", ProjectImpl.class)
                .setParameter("projectId", projectId);
        return query.getSingleResult();
    }
    @Override
    public Member updateMember(Member inputMember, Long userId) {
        return null;
    }

    @Override
    public void deleteMember(Long memberId, Long userId) {

    }
}
