package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;

public interface MemberService {
    MemberDto createMember(MemberDto memberDto);
    MemberDto getMember(Long memberId, Long userId);
    MemberDto updateMember(MemberDto memberDto);
    void deleteMember(Long memberId, Long userId);
}
