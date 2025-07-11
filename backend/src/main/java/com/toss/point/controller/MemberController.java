package com.toss.point.controller;

import com.toss.point.dto.MemberDto;
import com.toss.point.dto.PointHistoryDto;
import com.toss.point.response.Message;
import com.toss.point.response.Status;
import com.toss.point.service.MemberServiceImpl;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;


// 정렬 구현
@CrossOrigin({"*"})
@RestController
public class MemberController {

    @Autowired
    MemberServiceImpl memberService;

    @Value("${toss.secret-key}")
    private String secretKey;

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
    public ResponseEntity<?> pointCharge(@RequestBody String jsonBody) throws Exception {

        // Toss API는 paymentKey, orderId, amount 3가지만 받도록 정해진 형식의 JSON을 요구
        // amount는 숫자 데이터지만 Toss API는 요청 시 해당 데이터 타입을 String으로 요구
        // => 클라이언트에서 전송한 JSON에서 필요한 3가지 데이터만 추출하여 새 JSON 객체에 담아 API 요청
        JSONParser parser = new JSONParser();
        String orderId;
        String amount;
        String paymentKey;
        try {
            JSONObject requestData = (JSONObject) parser.parse(jsonBody);
            paymentKey = (String) requestData.get("paymentKey");
            orderId = (String) requestData.get("orderId");
            amount = requestData.get("amount").toString();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        };
        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", amount);
        obj.put("paymentKey", paymentKey);

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((secretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        // 결제 승인 요청
        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();

        System.out.println(jsonObject.toJSONString());

        // 포인트 충전 (결제를 요청한 토스 회원과 자사 회원의 신원 일치 여부 판단 기준 필요)
        PointHistoryDto result = memberService.chargePoint(Long.parseLong(orderId), Integer.parseInt(amount));
        Message msg = new Message(Status.OK, "포인트 충전이 정상적으로 완료되었습니다.", result);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

}
