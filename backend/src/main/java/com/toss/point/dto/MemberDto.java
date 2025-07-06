package com.toss.point.dto;

import com.toss.point.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MemberDto {

    private Long id;
    private String name;
    private int views;
    private String registerDate;

}
