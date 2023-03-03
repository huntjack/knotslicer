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

@ProcessAs(ProcessType.POLLANSWER)
@ApplicationScoped
@Transactional(rollbackOn={Exception.class})
public class PollAnswerDaoImpl implements ChildWithTwoParentsDao<PollAnswer,Poll,Member> {
    @PersistenceContext(unitName = "knotslicer_database")
    private EntityManager entityManager;

    @Override
    public PollAnswer create(PollAnswer pollAnswer, Long pollId, Long memberId) {
        PollAnswerImpl pollAnswerImpl = (PollAnswerImpl) pollAnswer;
        PollImpl pollImpl = getPollWithPollAnswersFromJpa(pollId);
        entityManager.detach(pollImpl);
        pollImpl.addPollAnswer(pollAnswerImpl);
        pollImpl = entityManager.merge(pollImpl);
        MemberImpl memberImpl = getMemberWithPollAnswersFromJpa(memberId);
        pollAnswerImpl = getPollAnswerFromPoll(pollImpl, pollAnswerImpl);
        memberImpl.addPollAnswer(pollAnswerImpl);
        entityManager.flush();
        return pollAnswerImpl;
    }
    private PollImpl getPollWithPollAnswersFromJpa(Long pollId) {
        TypedQuery<PollImpl> query = entityManager.createQuery
                        ("SELECT poll FROM Poll poll " +
                                "LEFT JOIN FETCH poll.pollAnswers " +
                                "WHERE poll.pollId = :pollId", PollImpl.class)
                .setParameter("pollId", pollId);
        return query.getSingleResult();
    }
    private MemberImpl getMemberWithPollAnswersFromJpa(Long memberId) {
        TypedQuery<MemberImpl> query = entityManager.createQuery
                        ("SELECT m FROM Member m " +
                                "LEFT JOIN FETCH m.pollAnswers " +
                                "WHERE m.memberId = :memberId", MemberImpl.class)
                .setParameter("memberId", memberId);
        return query.getSingleResult();
    }
    private PollAnswerImpl getPollAnswerFromPoll(PollImpl pollImpl, PollAnswer pollAnswer) {
        List<PollAnswerImpl> pollAnswerImpls = pollImpl.getPollAnswers();
        int pollAnswerIndex = pollAnswerImpls.indexOf(pollAnswer);
        return pollAnswerImpls.get(pollAnswerIndex);
    }
    @Override
    public Optional<PollAnswer> get(Long pollAnswerId) {
        PollAnswer pollAnswer = entityManager.find(PollAnswerImpl.class, pollAnswerId);
        return Optional.ofNullable(pollAnswer);
    }
    @Override
    public Poll getPrimaryParent(Long pollAnswerId) {
        TypedQuery<PollImpl> query = entityManager.createQuery(
                "SELECT poll FROM Poll poll " +
                        "INNER JOIN poll.pollAnswers pollAnswer " +
                        "WHERE pollAnswer.pollAnswerId = :pollAnswerId", PollImpl.class)
                .setParameter("pollAnswerId", pollAnswerId);
        return query.getSingleResult();
    }
    @Override
    public Member getSecondaryParent(Long pollAnswerId) {
        TypedQuery<MemberImpl> query = entityManager.createQuery(
                "SELECT m FROM Member m " +
                        "INNER JOIN m.pollAnswers pollAnswer " +
                        "WHERE pollAnswer.pollAnswerId = :pollAnswerId", MemberImpl.class)
                .setParameter("pollAnswerId", pollAnswerId);
        return query.getSingleResult();
    }
    @Override
    public Optional<Poll> getPrimaryParentWithChildren(Long pollId) {
        Poll poll = getPollWithPollAnswersFromJpa(pollId);
        return Optional.ofNullable(poll);
    }
    @Override
    public Optional<Member> getSecondaryParentWithChildren(Long memberId) {
        Member member = getMemberWithPollAnswersFromJpa(memberId);
        return Optional.ofNullable(member);
    }
    @Override
    public PollAnswer update(PollAnswer pollAnswerInput, Long pollId) {
        PollImpl pollImpl = getPollWithPollAnswersFromJpa(pollId);
        entityManager.detach(pollImpl);
        PollAnswer pollAnswerToBeModified =
                getPollAnswerFromPoll(
                        pollImpl,
                        pollAnswerInput);
        pollAnswerToBeModified.setApproved(pollAnswerInput.isApproved());
        pollImpl = entityManager.merge(pollImpl);
        entityManager.flush();
        return getPollAnswerFromPoll(
                pollImpl,
                pollAnswerToBeModified);
    }
    @Override
    public void delete(Long pollAnswerId) {
        Poll poll = getPrimaryParent(pollAnswerId);
        PollImpl pollImpl =
                getPollWithPollAnswersFromJpa(
                        poll.getPollId());
        PollAnswerImpl pollAnswerImpl = entityManager
                .find(PollAnswerImpl.class, pollAnswerId);
        pollImpl.removePollAnswer(pollAnswerImpl);
        entityManager.flush();
    }
}
