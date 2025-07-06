package com.toss.point.repository;

import com.toss.point.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepo extends JpaRepository<Member, Long> {
    public Member findMemberById(Long memberId);

}
