package com.toss.point.response;

import com.toss.point.dto.MemberDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {

    private Status status;

    private String message;

    private MemberDto result;

}
