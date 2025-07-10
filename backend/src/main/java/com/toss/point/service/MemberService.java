package com.toss.point.service;

import com.toss.point.dto.MemberDto;
import com.toss.point.dto.PointHistoryDto;
import org.springframework.data.domain.Page;

public interface MemberService {
    public MemberDto getMember(Long memberId);
    public MemberDto registerMember(MemberDto memberDto);
    public Page<MemberDto> getFilteredMemberList(int page, int size, String sortCol, String order);
    public void updateView(Long memberId);
    public PointHistoryDto chargePoint(Long memberId, int amount);
}
