package com.knotslicer.server.ports.interactor.services;


import com.knotslicer.server.domain.Member;
import com.knotslicer.server.ports.entitygateway.MemberDao;
import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;
import com.knotslicer.server.ports.interactor.mappers.EntityDtoMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class MemberServiceImpl implements MemberService {
    @Inject
    EntityDtoMapper entityDtoMapper;
    @Inject
    MemberDao memberDao;
    @Override
    public MemberDto createMember(MemberDto memberDto) {
        Member member = entityDtoMapper.toEntity(memberDto);
        member = memberDao.createMember(member, memberDto.getUserId(), memberDto.getProjectId());
        return entityDtoMapper.toDto(
                member,
                memberDto.getUserId(),
                memberDto.getProjectId());
    }
    @Override
    public MemberDto getMember(Long memberId, Long userId) {
        return null;
    }
    @Override
    public MemberDto updateMember(MemberDto memberDto) {
        return null;
    }
    @Override
    public void deleteMember(Long memberId, Long userId) {

    }
}
