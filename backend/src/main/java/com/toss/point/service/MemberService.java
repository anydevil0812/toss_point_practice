package com.toss.point.service;

import com.toss.point.dto.MemberDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {
    public MemberDto getMember(Long memberId);
    public MemberDto registerMember(MemberDto memberDto);
    public Page<MemberDto> getMemberList(Pageable pageable);
    public void updateView(Long memberId);
}
