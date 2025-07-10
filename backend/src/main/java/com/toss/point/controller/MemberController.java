package com.toss.point.controller;

import com.toss.point.dto.MemberDto;
import com.toss.point.dto.PointHistoryDto;
import com.toss.point.response.Message;
import com.toss.point.response.Status;
import com.toss.point.service.MemberServiceImpl;
import org.apache.tomcat.util.json.JSONParser;
import org.json.JSONException;
import org.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;


// 정렬 구현
@CrossOrigin({"*"})
@RestController
public class MemberController {

    @Autowired
    MemberServiceImpl memberService;

    // 회원 정보 조회
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

    // 회원 목록 조회
    @GetMapping("/memberList")
    public ResponseEntity<?> getFilteredMemberList(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "5") int size,
                                                   @RequestParam(required = false) String sortCol,
                                                   @RequestParam(defaultValue = "asc") String order) {

        Page<MemberDto> members = memberService.getFilteredMemberList(page, size, sortCol, order);
        Message msg = new Message(Status.OK, "회원 목록 조회 성공", members);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    // 회원 등록
    @PostMapping("/member")
    public ResponseEntity<?> registerMemberInfo(@RequestBody MemberDto memberDTO) {
        MemberDto result = memberService.registerMember(memberDTO);
        Message msg = new Message(Status.OK, "회원 등록이 정상적으로 완료되었습니다.", result);
        return new ResponseEntity<>(msg, HttpStatus.OK); // 예외처리로 회원가입 실패 구현 필요
    }

    // 조회수 1 증가
    @PutMapping("/member/views")
    public void updateViews(@RequestParam Long memberId) {
        memberService.updateView(memberId);
    }

    // 포인트 충전
    @PostMapping("/pointCharge")
    public ResponseEntity<?> pointCharge(@RequestBody String jsonBody) {

        // Toss API는 paymentKey, orderId, amount 3가지만 받도록 정해진 형식의 JSON을 요구
        // orderId, amount는 숫자 데이터지만 Toss API는 요청 시 해당 데이터 타입을 String으로 요구
        // => 클라이언트에서 전송한 JSON에서 필요한 3가지 데이터만 추출하여 새 JSON 객체에 담아 API 요청
        String orderId;
        String amount;
        String paymentKey;

        try {
            JSONObject requestData = new JSONObject(jsonBody);
            paymentKey = requestData.getString("paymentKey");
            orderId = requestData.getString("orderId");
            amount = requestData.get("amount").toString();

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", amount);
        obj.put("paymentKey", paymentKey);

//        // 시크릿 키 인코딩
//        String secretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6"; // 실제 서비스 시 변경
//        String encodedAuth = Base64.getEncoder().encodeToString((secretKey + ":").getBytes(StandardCharsets.UTF_8));
//        String authorizationHeader = "Basic " + encodedAuth;
//
//        // WebClient 인스턴스 생성
//        WebClient webClient = WebClient.builder()
//                                       .baseUrl("https://api.tosspayments.com/v1")
//                                       .defaultHeader("Authorization", authorizationHeader)
//                                       .defaultHeader("Content-Type", "application/json")
//                                       .build();
//
//        // API 호출 및 응답 처리
//        String responseBody = webClient.post()
//                                       .uri("/payments/confirm")
//                                       .bodyValue(obj.toString())
//                                       .retrieve()
//                                       .onStatus(status -> status.isError(), clientResponse -> clientResponse.bodyToMono(String.class)
//                                                .flatMap(error -> Mono.error(new RuntimeException("결제 승인 실패: " + error))))
//                                       .bodyToMono(String.class)
//                                       .block(); // 동기 처리

        // 상황상 실제 토스 회원 ID 조회는 불가능하므로 토스 회원 ID와 자사 회원 ID가 동일하다는 가정하에 개발 진행
        PointHistoryDto result = memberService.chargePoint(Long.parseLong(orderId), Integer.parseInt(amount));
        Message msg = new Message(Status.OK, "포인트 충전이 정상적으로 완료되었습니다.", result);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

}
