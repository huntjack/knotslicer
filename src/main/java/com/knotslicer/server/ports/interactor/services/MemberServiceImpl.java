package com.knotslicer.server.ports.interactor.services;


import com.knotslicer.server.domain.Member;
import com.knotslicer.server.domain.User;
import com.knotslicer.server.ports.entitygateway.MemberDao;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.exceptions.EntityNotFoundException;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
@MemberService
public class MemberServiceImpl implements Service<MemberDto> {
    @Inject
    EntityDtoMapper entityDtoMapper;
    @Inject
    MemberDao memberDao;

    @Override
    public MemberDto create(MemberDto memberDto) {
        Member member = entityDtoMapper.toEntity(memberDto);
        member = memberDao.create(member, memberDto.getUserId(), memberDto.getProjectId());
        return entityDtoMapper.toDto(
                member,
                memberDto.getUserId(),
                memberDto.getProjectId());
    }
    @Override
    public MemberDto get(Long memberId, Long userId) {
        Optional<Member> optionalMember = memberDao.get(memberId);
        Member member = unpackOptionalProject(optionalMember);
        Long projectId = memberDao.getProjectId(memberId);
        return entityDtoMapper.toDto(member, userId, projectId);
    }
    @Override
    public MemberDto getWithChildren(Long memberId, Long userId) {
        return null;
    }
    private Member unpackOptionalProject(Optional<Member> optionalMember) {
        return optionalMember.orElseThrow(() -> new EntityNotFoundException("Member not found."));
    }
    @Override
    public MemberDto update(MemberDto memberDto) {
        return null;
    }
    @Override
    public void delete(Long memberId, Long userId) {
        memberDao.delete(memberId, userId);
    }
}
