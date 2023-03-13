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
        PollImpl pollWithPollAnswers = getPollImplWithPollAnswers(pollId);
        entityManager.detach(pollWithPollAnswers);
        pollWithPollAnswers.addPollAnswer(pollAnswerImpl);
        pollWithPollAnswers = entityManager.merge(pollWithPollAnswers);
        Optional<Member> optionalMemberWithPollAnswers =
                getSecondaryParentWithChildren(memberId);
        MemberImpl memberWithPollAnswers =
                (MemberImpl) optionalMemberWithPollAnswers
                        .orElseThrow(() -> new EntityNotFoundException());
        Optional<PollAnswerImpl> optionalPollAnswer =
                getPollAnswerFromPoll(pollWithPollAnswers, pollAnswerImpl);
        pollAnswerImpl = optionalPollAnswer
                .orElseThrow(() -> new EntityNotFoundException());
        memberWithPollAnswers.addPollAnswer(pollAnswerImpl);
        entityManager.flush();
        return pollAnswerImpl;
    }
    private PollImpl getPollImplWithPollAnswers(Long pollId) {
        Optional<Poll> optionalPollWithPollAnswers = getPrimaryParentWithChildren(pollId);
        return (PollImpl) optionalPollWithPollAnswers
                .orElseThrow(() -> new EntityNotFoundException());
    }
    @Override
    public Optional<Poll> getPrimaryParentWithChildren(Long pollId) {
        TypedQuery<PollImpl> query = entityManager.createQuery
                        ("SELECT poll FROM Poll poll " +
                                "LEFT JOIN FETCH poll.pollAnswers " +
                                "WHERE poll.pollId = :pollId", PollImpl.class)
                .setParameter("pollId", pollId);
        Poll poll = query.getSingleResult();
        return Optional.ofNullable(poll);
    }
    @Override
    public Optional<Member> getSecondaryParentWithChildren(Long memberId) {
        TypedQuery<MemberImpl> query = entityManager.createQuery
                        ("SELECT m FROM Member m " +
                                "LEFT JOIN FETCH m.pollAnswers " +
                                "WHERE m.memberId = :memberId", MemberImpl.class)
                .setParameter("memberId", memberId);
        Member member = query.getSingleResult();
        return Optional.ofNullable(member);
    }
    private Optional<PollAnswerImpl> getPollAnswerFromPoll(PollImpl pollImpl, PollAnswer pollAnswer) {
        List<PollAnswerImpl> pollAnswerImpls = pollImpl.getPollAnswers();
        int pollAnswerIndex = pollAnswerImpls.indexOf(pollAnswer);
        PollAnswerImpl pollAnswerImpl =
                pollAnswerImpls.get(pollAnswerIndex);
        return Optional.ofNullable(pollAnswerImpl);
    }
    @Override
    public Optional<PollAnswer> get(Long pollAnswerId) {
        PollAnswer pollAnswer = entityManager.find(PollAnswerImpl.class, pollAnswerId);
        return Optional.ofNullable(pollAnswer);
    }
    @Override
    public Optional<Poll> getPrimaryParent(Long pollAnswerId) {
        TypedQuery<PollImpl> query = entityManager.createQuery(
                "SELECT poll FROM Poll poll " +
                        "INNER JOIN poll.pollAnswers pollAnswer " +
                        "WHERE pollAnswer.pollAnswerId = :pollAnswerId", PollImpl.class)
                .setParameter("pollAnswerId", pollAnswerId);
        Poll poll = query.getSingleResult();
        return Optional.ofNullable(poll);
    }
    @Override
    public Optional<Member> getSecondaryParent(Long pollAnswerId) {
        TypedQuery<MemberImpl> query = entityManager.createQuery(
                "SELECT m FROM Member m " +
                        "INNER JOIN m.pollAnswers pollAnswer " +
                        "WHERE pollAnswer.pollAnswerId = :pollAnswerId", MemberImpl.class)
                .setParameter("pollAnswerId", pollAnswerId);
        Member member = query.getSingleResult();
        return Optional.ofNullable(member);
    }
    @Override
    public PollAnswer update(PollAnswer pollAnswerInput, Long pollId) {
        PollImpl pollWithPollAnswers = getPollImplWithPollAnswers(pollId);
        Optional<PollAnswerImpl> optionalPollAnswerToBeModified =
                getPollAnswerFromPoll(
                        pollWithPollAnswers,
                        pollAnswerInput);
        PollAnswerImpl pollAnswerToBeModified = optionalPollAnswerToBeModified
                .orElseThrow(() -> new EntityNotFoundException());
        entityManager.detach(pollWithPollAnswers);
        pollAnswerToBeModified.setApproved(pollAnswerInput.isApproved());
        pollWithPollAnswers = entityManager.merge(pollWithPollAnswers);
        entityManager.flush();
        Optional<PollAnswerImpl> optionalPollAnswerResponse =
                getPollAnswerFromPoll(
                        pollWithPollAnswers,
                        pollAnswerToBeModified);
        return optionalPollAnswerResponse
                .orElseThrow(() -> new EntityNotFoundException());
    }
    @Override
    public void delete(Long pollAnswerId) {
        Optional<Poll> optionalPoll = getPrimaryParent(pollAnswerId);
        Poll poll = optionalPoll
                .orElseThrow(() -> new EntityNotFoundException());
        Long pollId = poll.getPollId();
        PollImpl pollWithPollAnswers = getPollImplWithPollAnswers(pollId);
        Optional<PollAnswer> optionalPollAnswer = get(pollAnswerId);
        PollAnswerImpl pollAnswerImpl = (PollAnswerImpl) optionalPollAnswer
                .orElseThrow(() -> new EntityNotFoundException());
        pollWithPollAnswers.removePollAnswer(pollAnswerImpl);
        entityManager.flush();
    }
}
