package com.knotslicer.server.ports.interactor.services;


import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Project;
import com.knotslicer.server.domain.Schedule;
import com.knotslicer.server.domain.User;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.entitygateway.MemberDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ApplicationScoped
public class MemberServiceImpl implements MemberService {
    private EntityDtoMapper entityDtoMapper;
    private MemberDao memberDao;
    private ChildWithOneRequiredParentDao<Schedule, Member> scheduleDao;

    @Override
    public MemberDto create(MemberDto memberDto) {
        Member member = entityDtoMapper
                .toEntity(memberDto);
        Long userId = memberDto.getUserId();
        Long projectId = memberDto.getProjectId();
        member = memberDao.create(
                member,
                userId,
                projectId);
        return entityDtoMapper.toDto(
                member,
                userId,
                projectId);
    }
    @Override
    public MemberDto get(Long memberId) {
        Optional<Member> optionalMember = memberDao
                .get(memberId);
        Member member = unpackOptionalMember(optionalMember);
        User user = memberDao.getPrimaryParent(memberId);
        Long userId = user.getUserId();
        Project project = memberDao.getSecondaryParent(memberId);
        Long projectId = project.getProjectId();;
        return entityDtoMapper.toDto(
                member,
                userId,
                projectId);
    }
    private Member unpackOptionalMember(Optional<Member> optionalMember) {
        return optionalMember.orElseThrow(() -> new EntityNotFoundException("Member not found."));
    }
    @Override
    public MemberDto getWithChildren(Long memberId) {
        Optional<Member> optionalMember = scheduleDao.getPrimaryParentWithChildren(memberId);
        Member member = unpackOptionalMember(optionalMember);
        User user = memberDao.getPrimaryParent(memberId);
        Long userId = user.getUserId();
        Project project = memberDao.getSecondaryParent(memberId);
        Long projectId = project.getProjectId();
        MemberDto memberDto =
                entityDtoMapper.toDto(
                        member,
                        userId,
                        projectId);
        return entityDtoMapper
                .addScheduleDtosToMemberDto(
                        memberDto,
                        member);
    }
    @Override
    public MemberDto getWithEvents(Long memberId) {
        return null;
    }
    @Override
    public MemberDto update(MemberDto memberDto) {
        Long memberId = memberDto.getMemberId();
        Optional<Member> optionalMember =
                memberDao.get(memberId);
        Member memberToBeModified = unpackOptionalMember(optionalMember);

        memberToBeModified = entityDtoMapper
                .toEntity(memberDto,
                        memberToBeModified);

        User user = memberDao.getPrimaryParent(memberId);
        Long userId = user.getUserId();
        Member updatedMember =
                memberDao.update(
                        memberToBeModified,
                        userId);

        Project project = memberDao.getSecondaryParent(memberId);
        Long projectId = project.getProjectId();
        return entityDtoMapper.toDto(
                updatedMember,
                userId,
                projectId);
    }
    @Override
    public void delete(Long memberId) {
        memberDao.delete(memberId);
    }
    @Inject
    public MemberServiceImpl(EntityDtoMapper entityDtoMapper,
                             MemberDao memberDao,
                             @ProcessAs(ProcessType.SCHEDULE)
                             ChildWithOneRequiredParentDao<Schedule, Member> scheduleDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.memberDao = memberDao;
        this.scheduleDao = scheduleDao;
    }
    protected MemberServiceImpl() {}
}
