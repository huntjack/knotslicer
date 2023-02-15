package com.knotslicer.server.ports.interactor.services;


import com.knotslicer.server.domain.Member;
import com.knotslicer.server.ports.entitygateway.MemberDao;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberLightDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.HashMap;
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
        memberDto.setMemberId(
                member.getMemberId());
        Map<String,Long> primaryKeys =
                addExtraKeys(memberDto);
        return entityDtoMapper.toDto(
                member,
                primaryKeys);
    }
    private Map<String,Long> addExtraKeys(MemberDto memberDto) {
        Map<String,Long> primaryKeys = new HashMap<>();
        primaryKeys.put(
                "userId",
                memberDto.getUserId());
        Long memberId = memberDto.getMemberId();
        Long projectId = memberDao
                .getSecondaryParentId(memberId);
        primaryKeys.put("projectId", projectId);
        Long projectOwnerId = memberDao
                .getParentIdOfSecondaryParent(projectId);
        primaryKeys.put("projectOwnerId", projectOwnerId);
        return primaryKeys;
    }
    @Override
    public MemberDto get(Map<String,Long> primaryKeys) {
        Long memberId = primaryKeys.get("memberId");
        Optional<Member> optionalMember = memberDao
                .get(memberId);
        Member member = unpackOptionalMember(optionalMember);
        addExtraKeys(primaryKeys);
        return entityDtoMapper.toDto(
                member,
                primaryKeys);
    }
    private void addExtraKeys(Map<String,Long> primaryKeys) {
        Long memberId = primaryKeys.get("memberId");
        Long projectId = memberDao
                .getSecondaryParentId(memberId);
        primaryKeys.put("projectId", projectId);
        Long projectOwnerId = memberDao
                .getParentIdOfSecondaryParent(projectId);
        primaryKeys.put("projectOwnerId", projectOwnerId);
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

        Map<String, Long> primaryKeys = addExtraKeys(memberDto);
        return entityDtoMapper.toDto(
                updatedMember,
                primaryKeys);
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
