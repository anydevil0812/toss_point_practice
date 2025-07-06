package com.toss.point.controller;

import com.toss.point.dto.MemberDto;
import com.toss.point.response.Message;
import com.toss.point.response.Status;
import com.toss.point.service.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin({"*"})
@RestController
public class MemberController {

    @Autowired
    MemberServiceImpl memberService;

    @GetMapping("/member")
    public ResponseEntity<?> getMemberInfo(@RequestParam Long memberId) {
        MemberDto memberDTO = memberService.getMember(memberId);
        if (memberDTO != null) {
            Message msg = new Message(Status.OK, "회원 정보 조회가 정상적으로 실행되었습니다.", memberDTO);
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }
        Message msg = new Message(Status.OK, "회원 정보 조회에 실패하였습니다.", null);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PostMapping("/member")
    public ResponseEntity<?> registerMemberInfo(@RequestBody MemberDto memberDTO) {
        System.out.println("멤버 : " + memberDTO);
        MemberDto result = memberService.registerMember(memberDTO);
        Message msg = new Message(Status.OK, "회원 등록이 정상적으로 완료되었습니다.", result);
        return new ResponseEntity<>(msg, HttpStatus.OK); // 예외처리로 회원가입 실패 구현 필요
    }

}
