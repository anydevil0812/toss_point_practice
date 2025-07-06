package com.toss.point.controller;

import com.toss.point.dto.MemberDto;
import com.toss.point.response.Message;
import com.toss.point.response.Status;
import com.toss.point.service.MemberServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

public class MemberController {

    @Autowired
    MemberServiceImpl memberService;

    @GetMapping("/member")
    public ResponseEntity<?> getMask(@RequestParam Long memberId) {
        MemberDto memberDTO = memberService.getMember(memberId);
        if (memberDTO != null) {
            Message msg = new Message(Status.OK, "회원 정보 조회가 정상적으로 실행되었습니다.", memberDTO);
            return new ResponseEntity<>(msg, HttpStatus.OK);
        }
        Message msg = new Message(Status.OK, "회원 정보 조회에 실패하였습니다.", null);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

}
