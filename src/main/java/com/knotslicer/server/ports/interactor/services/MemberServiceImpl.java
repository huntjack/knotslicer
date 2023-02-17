package com.knotslicer.server.ports.interactor.services;


import com.knotslicer.server.domain.Member;
import com.knotslicer.server.ports.entitygateway.MemberDao;
import com.knotslicer.server.ports.entitygateway.ScheduleDao;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@MemberService
@ApplicationScoped
public class MemberServiceImpl implements ParentService<MemberDto> {
    private EntityDtoMapper entityDtoMapper;
    private MemberDao memberDao;
    private ScheduleDao scheduleDao;

    @Override
    public MemberDto create(MemberDto memberDto) {
        Member member = entityDtoMapper
                .toEntity(memberDto);
        member = memberDao.create(
                member,
                memberDto.getUserId(),
                memberDto.getProjectId());
        memberDto.setMemberId(
                member.getMemberId());
        Map<String,Long> ids =
                addExtraIds(memberDto);
        return entityDtoMapper.toDto(
                member,
                ids);
    }
    private Map<String,Long> addExtraIds(MemberDto memberDto) {
        Map<String,Long> ids = new HashMap<>();
        ids.put("userId",
                memberDto.getUserId());
        Long memberId = memberDto.getMemberId();
        Long projectId = memberDao
                .getSecondaryParentId(memberId);
        ids.put("projectId", projectId);
        Long projectOwnerId = memberDao
                .getParentIdOfSecondaryParent(projectId);
        ids.put("projectOwnerId", projectOwnerId);
        return ids;
    }
    @Override
    public MemberDto get(Map<String,Long> ids) {
        Long memberId = ids.get("memberId");
        Optional<Member> optionalMember = memberDao
                .get(memberId);
        Member member = unpackOptionalMember(optionalMember);
        addExtraIds(ids);
        return entityDtoMapper.toDto(
                member,
                ids);
    }
    private void addExtraIds(Map<String,Long> ids) {
        Long memberId = ids.get("memberId");
        Long projectId = memberDao
                .getSecondaryParentId(memberId);
        ids.put("projectId", projectId);
        Long projectOwnerId = memberDao
                .getParentIdOfSecondaryParent(projectId);
        ids.put("projectOwnerId", projectOwnerId);
    }
    private Member unpackOptionalMember(Optional<Member> optionalMember) {
        return optionalMember.orElseThrow(() -> new EntityNotFoundException("Member not found."));
    }
    @Override
    public MemberDto getWithChildren(Map<String,Long> ids) {
        Long memberId = ids.get("memberId");
        Optional<Member> optionalMember = scheduleDao.getPrimaryParentWithChildren(memberId);
        Member member = unpackOptionalMember(optionalMember);
        addExtraIds(ids);
        MemberDto memberDto = entityDtoMapper.toDto(member, ids);
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

        Long userId = memberDto.getUserId();
        Member updatedMember =
                memberDao.update(memberToBeModified, userId);

        Map<String, Long> ids = addExtraIds(memberDto);
        return entityDtoMapper.toDto(
                updatedMember,
                ids);
    }
    @Override
    public void delete(Map<String,Long> ids) {
        Long memberId = ids.get("memberId");
        Long userId = ids.get("userId");
        memberDao.delete(
                memberId,
                userId);
    }
    @Inject
    public MemberServiceImpl(EntityDtoMapper entityDtoMapper, MemberDao memberDao, ScheduleDao scheduleDao) {
        this.entityDtoMapper = entityDtoMapper;
        this.memberDao = memberDao;
        this.scheduleDao = scheduleDao;
    }
    protected MemberServiceImpl() {}
}
