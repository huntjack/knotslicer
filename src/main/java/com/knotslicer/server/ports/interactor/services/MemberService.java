package com.knotslicer.server.ports.interactor.services;

import com.knotslicer.server.ports.interactor.datatransferobjects.MemberDto;

public interface MemberService extends ParentService<MemberDto> {
    MemberDto getWithEvents(Long memberId);
}
