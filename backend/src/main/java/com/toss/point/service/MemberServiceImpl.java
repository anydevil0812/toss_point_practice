package com.toss.point.service;

import com.toss.point.dto.MemberDto;
import com.toss.point.entity.Member;
import com.toss.point.repository.MemberRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    MemberRepo memberRepo;

    @Override
    @Transactional
    public MemberDto getMember(Long memberId) {
        Member member = memberRepo.findMemberById(memberId);
        return Member.entityToDTO(member);
    }

    @Override
    @Transactional
    public MemberDto registerMember(MemberDto memberDto) {
        memberRepo.save(MemberDto.DtoToEntity(memberDto));
        return memberDto;
    }

    @Override
    @Transactional
    public Page<MemberDto> getMemberList(Pageable pageable) {
        return memberRepo.findAll(pageable).map(Member::entityToDTO);
    }

    @Override
    @Transactional
    public void updateView(Long memberId) {
        Member member = memberRepo.findMemberById(memberId);
        member.updateView();
        memberRepo.save(member);
    }

}
