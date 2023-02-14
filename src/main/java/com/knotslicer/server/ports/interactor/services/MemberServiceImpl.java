package com.knotslicer.server.ports.interactor.services;


import com.knotslicer.server.domain.Member;
import com.knotslicer.server.ports.entitygateway.MemberDao;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberLightDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Map;
import java.util.Optional;

@MemberService
@ApplicationScoped
public class MemberServiceImpl implements Service<MemberDto> {
    @Inject
    EntityDtoMapper entityDtoMapper;
    @Inject
    MemberDao memberDao;

    @Override
    public MemberDto create(MemberDto memberDto) {
        Member member = entityDtoMapper
                .toEntity(memberDto);
        member = memberDao.create(
                member,
                memberDto.getUserId(),
                memberDto.getProjectId());
        Long projectId = memberDto
                .getProjectId();
        Long projectOwnerId = memberDao
                .getParentIdOfSecondaryParent(projectId);
        return entityDtoMapper.toDto(
                member,
                memberDto.getUserId(),
                projectId, projectOwnerId);
    }
    @Override
    public MemberDto get(Map<String,Long> primaryKeys) {
        Long memberId = primaryKeys.get("memberId");
        Optional<Member> optionalMember = memberDao
                .get(memberId);
        Member member = unpackOptionalMember(optionalMember);
        Long projectId = memberDao
                .getSecondaryParentId(memberId);
        Long projectOwnerId = memberDao
                .getParentIdOfSecondaryParent(projectId);
        Long userId = primaryKeys
                .get("userId");
        return entityDtoMapper.toDto(member,
                userId,
                projectId,
                projectOwnerId);
    }
    private Member unpackOptionalMember(Optional<Member> optionalMember) {
        return optionalMember.orElseThrow(() -> new EntityNotFoundException("Member not found."));
    }
    @Override
    public MemberDto getWithChildren(Map<String,Long> primaryKeys) {
        return null;
    }

    @Override
    public MemberDto update(MemberDto memberDto) {
        Long memberId = memberDto.getMemberId();
        Optional<Member> optionalMember =
                memberDao.get(memberId);
        Member memberToBeModified = unpackOptionalMember(optionalMember);
        memberToBeModified = entityDtoMapper
                .toEntity(
                        memberDto,
                        memberToBeModified);
        Long userId = memberDto.getUserId();
        Member updatedMember = memberDao
                .update(memberToBeModified, userId);
        Long projectId = memberDao
                .getSecondaryParentId(memberId);
        Long projectOwnerId = memberDao
                .getParentIdOfSecondaryParent(projectId);
        return entityDtoMapper.toDto(
                updatedMember,
                userId,
                projectId,
                projectOwnerId);
    }
    @Override
    public void delete(Map<String,Long> primaryKeys) {
        Long memberId = primaryKeys.get("memberId");
        Long userId = primaryKeys.get("userId");
        memberDao.delete(
                memberId,
                userId);
    }
}
