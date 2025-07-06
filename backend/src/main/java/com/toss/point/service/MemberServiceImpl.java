package com.toss.point.service;

import com.toss.point.dto.MemberDto;
import com.toss.point.entity.Member;
import com.toss.point.repository.MemberRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

public class MemberServiceImpl {

    @Autowired
    MemberRepo memberRepo;

    @Transactional
    public MemberDto getMember(Long memberId) {
        Member member = memberRepo.findMemberById(memberId);
        return Member.entityToDTO(member);
    }
    
}
