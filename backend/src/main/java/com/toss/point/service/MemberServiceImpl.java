package com.toss.point.service;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.toss.point.dto.MemberDto;
import com.toss.point.dto.PointHistoryDto;
import com.toss.point.entity.Member;
import com.toss.point.entity.PointHistory;
import com.toss.point.entity.QMember;
import com.toss.point.repository.MemberRepo;
import com.toss.point.repository.PointHistoryRepo;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl implements MemberService {

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Autowired
    public MemberServiceImpl(EntityManager em) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Autowired
    MemberRepo memberRepo;

    @Autowired
    PointHistoryRepo pointHistoryRepo;

    @Override
    @Transactional // 특정 회원 정보 조회
    public MemberDto getMember(Long memberId) {
        Member member = memberRepo.findMemberById(memberId);
        return Member.entityToDTO(member);
    }

    @Override
    @Transactional // 회원 등록
    public MemberDto registerMember(MemberDto memberDto) {
        memberRepo.save(MemberDto.DtoToEntity(memberDto));
        return memberDto;
    }

    @Override
    @Transactional // 회원 목록 조회
    public Page<MemberDto> getFilteredMemberList(int page, int size, String sortCol, String order) {

        QMember member = QMember.member;
        OrderSpecifier<?> orderSpecifier = getOrderSpecifier(sortCol, order, member);

        List<Member> content = queryFactory.selectFrom(member)
                                           .orderBy(orderSpecifier)
                                           .offset((long) page * size) // 산술 오버플로우 방지
                                           .limit(size)
                                           .fetch();

        Long total = queryFactory.select(member.count()).from(member).fetchOne();
        List<MemberDto> dtoList = content.stream().map(Member::entityToDTO).collect(Collectors.toList());

        return new PageImpl<>(dtoList, PageRequest.of(page, size), Optional.ofNullable(total).orElse(0L)); // Optional -> NPE 방지
    }

    // 정렬 조건 생성
    private OrderSpecifier<?> getOrderSpecifier(String sortCol, String order, QMember member) {
        Order direction = "desc".equalsIgnoreCase(order) ? Order.DESC : Order.ASC;

        switch (sortCol) {
            case "name":
                return new OrderSpecifier<>(direction, member.name);
            case "views":
                return new OrderSpecifier<>(direction, member.views);
            case "registerDate":
                return new OrderSpecifier<>(direction, member.registerDate);
            default:
                return new OrderSpecifier<>(Order.ASC, member.name);
        }
    }

    @Override
    @Transactional // 조회수 1 증가
    public void updateView(Long memberId) {
        Member member = memberRepo.findMemberById(memberId);
        member.updateView();
        memberRepo.save(member);
    }

    @Override
    @Transactional // 포인트 충전
    public PointHistoryDto chargePoint(Long memberId, int amount) {
        Member member = memberRepo.findMemberById(memberId);
        member.chargePoint(amount);
        memberRepo.save(member);

        PointHistory ph = PointHistory.createEntity(member, amount);
        pointHistoryRepo.save(ph);
        return PointHistory.entityToDTO(ph);
    }

}
