package com.knotslicer.server.ports.interactor.services;


import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.Project;
import com.knotslicer.server.domain.Schedule;
import com.knotslicer.server.domain.User;
import com.knotslicer.server.ports.entitygateway.ChildWithOneRequiredParentDao;
import com.knotslicer.server.ports.entitygateway.ChildWithTwoParentsDao;
import com.knotslicer.server.ports.interactor.ProcessAs;
import com.knotslicer.server.ports.interactor.ProcessType;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.Optional;

@ProcessAs(ProcessType.MEMBER)
@ApplicationScoped
public class MemberServiceImpl implements ParentService<MemberDto> {
    private EntityDtoMapper entityDtoMapper;
    private ChildWithTwoParentsDao<Member,User,Project> memberDao;
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
        Long userId = memberDao.getPrimaryParentId(memberId);
        Long projectId = memberDao.getSecondaryParentId(memberId);
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
        Long userId = memberDao.getPrimaryParentId(memberId);
        Long projectId = memberDao.getSecondaryParentId(memberId);
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
    public MemberDto update(MemberDto memberDto) {
        Long memberId = memberDto.getMemberId();
        Optional<Member> optionalMember =
                memberDao.get(memberId);
        Member memberToBeModified = unpackOptionalMember(optionalMember);

        memberToBeModified = entityDtoMapper
                .toEntity(memberDto,
                        memberToBeModified);

        Long userId = memberDao.getPrimaryParentId(memberId);
        Member updatedMember =
                memberDao.update(memberToBeModified, userId);

        Long projectId = memberDao.getSecondaryParentId(memberId);
        return entityDtoMapper.toDto(
                updatedMember,
                userId,
                projectId);
    }
    @Override
    public void delete(Long memberId) {
        Long userId = memberDao.getPrimaryParentId(memberId);
        memberDao.delete(
                memberId,
                userId);
    }
    @Inject
    public MemberServiceImpl(EntityDtoMapper entityDtoMapper,
                             @ProcessAs(ProcessType.MEMBER)
                             ChildWithTwoParentsDao<Member, User, Project> memberDao,
                             @ProcessAs(ProcessType.SCHEDULE)
                             ChildWithOneRequiredParentDao<Schedule, Member> scheduleDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.memberDao = memberDao;
        this.scheduleDao = scheduleDao;
    }
    protected MemberServiceImpl() {}
}
